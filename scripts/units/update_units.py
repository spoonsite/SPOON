import requests
import json
import sys
import pickle
from parens import find_parens
from common_functions import computeDuplicates, checkUnit, checkUnitList, TOKEN, SESSION_ID


"""
This script can (1) generate a report and (2) process attributes.

1) Generate a Report

    The report consists of:
        - attributes that have no unit
        - duplicate attributes with the same name

2) Process attributes

    It goes through each attribute and suggests a new name for
    the attribute and suggests a unit.

    Most attribute names currently are in the form
    '<Attr Name> (unit)'. The script will strip out
    the unit and suggest a unit and hit the endpoint to update
    the attribute.
"""

# Get your SESSION_ID from the web page after you login to the
# application
# SESSION_ID = '8976097DDA38E0B2E7830A925FBD6D9C'
# TOKEN = ''

COOKIES = {
    'JSESSIONID': SESSION_ID,
}
HEADERS = {
    'Cookie': f'JSESSIONID={SESSION_ID};',
    'Content-Type': 'application/json',
}
BASE_URL = 'http://localhost:8080/openstorefront'
ENDPOINT = '/api/v1/resource/attributes/attributetypes'

SKIPPED_FILENAME = 'skipped.txt'
UNIT_LIST_MAP = {}
ATTRIBUTE_MAP = {}

# parsed = json.loads(r.text)
# print(json.dumps(parsed, indent=4))
def prettyPrint(r):
    print("URL: ", BASE_URL + ENDPOINT)
    print("STATUS: ", r.status_code)
    parsed = json.loads(r.text)
    print(json.dumps(parsed, indent=4))

##########################Get suggested units##########################
def getUnit(original_description, attribute, unit, description):
    old_unit = unit
    old_description = description
    user_input = input('Use the suggested description and unit? [Yns]')
    if user_input == 'N' or user_input == 'n':
        unit        = input(f'Unit [{old_unit}]: ')
        description = input(f'Description [{old_description}]: ')
    if user_input == 'S' or user_input == 's' or user_input == 'skip':
        print('skipping attribute')
        with open(SKIPPED_FILENAME, 'a') as fout:
            fout.write(f"{attribute['attributeType']}\n")
        return
    if unit == '':
        unit = old_unit
    if description == '':
        description = old_description
    attribute['attributeUnit'] = unit
    attribute['description'] = description

    print()
    print(f'Unit:            {unit}')
    print(f'Description:     {description}')
    checkUnit(unit)
    user_input = input('Is this correct? [Yn]')
    if user_input == 'N' or user_input == 'n':
        return getUnit(original_description, attribute, old_unit, old_description)
    else:
        getUnitList(original_description, attribute, unit)

########################Get suggested unit list or make new one########################
def getUnitList(original_description, attribute, unit, old_unit_list=[]):
    global ATTRIBUTE_MAP
    global UNIT_LIST_MAP
    unit_list = []
    if unit in UNIT_LIST_MAP.keys():
        unit_list = UNIT_LIST_MAP[unit]
        print(f'Unit List: {unit_list}')
        user_input = input('Use the suggested unit list? [Yn]')

        if user_input == 'N' or user_input == 'n':
            unit_list = input(f'Unit List {old_unit_list}: ')
            unit_list = [ i.strip() for i in unit_list.split(',') ]
            UNIT_LIST_MAP[unit] = unit_list
        if unit_list == [""]:
            unit_list = old_unit_list
        attribute['attributeUnitList'] = unit_list
    else:
        unit_list = input(f'Provide a comman separated unit list: ')
        unit_list = [ i.strip() for i in unit_list.split(',') ]
        attribute['attributeUnitList'] = unit_list
        UNIT_LIST_MAP[unit] = unit_list

    print()
    print(f'Unit List:            {unit_list}')
    checkUnitList(unit, unit_list)
    user_input = input('Is this correct? [Yn]')
    if user_input == 'N' or user_input == 'n':
        return getUnitList(original_description, attribute, unit, old_unit_list)
    else:
        updateAttribute(original_description, attribute)

####################### Post to endpoint with the striped unit ###############################
def updateAttribute(original_description, attribute):
    global ATTRIBUTE_MAP
    global UNIT_LIST_MAP
    attributeType = attribute['attributeType']
    data = {
        'attributeType': {
            'allowMultipleFlg'       : True,
            'allowUserGeneratedCodes': True,
            'attributeType'          : attribute['attributeType'],
            'attributeUnit'          : attribute['attributeUnit'],
            'attributeUnitList'      : attribute['attributeUnitList'] if 'attributeUnitList' in attribute.keys() else '',
            'attributeValueType'     : 'NUMBER',             # attribute['attributeValueType'] if 'attributeValueType' in attribute.keys() else '',
            'defaultAttributeCode'   : attribute['defaultAttributeCode'] if 'defaultAttributeCode' in attribute.keys() else '',
            'description'            : attribute['description'] if 'description' in attribute.keys() else '',
            'detailedDescription'    : attribute['detailedDescription'] if 'detailedDescription' in attribute.keys() and len(attribute['detailedDescription']) < 200 else '',
        },
        # 'optionalComponentTypes': attribute['optionalComponentTypes'], # these lines break the endpoint request
        # 'requiredComponentType' : attribute['requiredComponentType']
    }
    print(json.dumps(data, indent=4))
    print('updating the attribute...')
    res = requests.put(f'{BASE_URL}/api/v1/resource/attributes/attributetypes/{attributeType}', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    print(res)
    try:
        prettyPrint(res)
        if original_description not in ATTRIBUTE_MAP.keys():
            ATTRIBUTE_MAP[original_description] = attribute
    except:
        print('unable to parse the response')

########################## Entry Point for dealing with attributes ##########################
def prompt(attribute):
    unit = ''
    des = attribute['description']
    description = ''
    attributeType = attribute['attributeType']

    print('-------------------------------------')
    print()
    print(f'Attribute Description:       {des}')
    print(f'Attribute Type:              {attributeType}')
    if '(' in des:
        start, end = find_parens(des)[-1]
        # start = des.find('(')
        # end   = des.find(')')
        unit  = des[start+1:end].strip()
        description = des[:start] + des[end+1:].strip()
        print()
        print(f'Suggested unit:              {unit}')
        print(f'Suggested description:       {description}')
    else:
        unit = ''
        description = des
    print()

    # some helpers to make help provide a suggestion for units
    if '-' in unit:
        unit = unit.replace('-', '*')
    if 'degrees' in unit:
        unit = unit.replace('degrees', 'deg')
    if unit == 'sec':
        unit = 's'
    if unit == 'Deg':
        unit = 'deg'
    if unit == 'Nm':
        unit = 'N*m'
    if unit == 'none' or unit == 'None':
        unit = ''

    checkUnit(unit)

    print('-------------------------------------')
    getUnit(des, attribute, unit, description)


######################### print duplicates #########################
def printDups(dups, attributes):
    print(f'Found {len(dups)} duplicates')
    prev = ''
    cur  = ''
    count = 0
    for i in attributes:
        if i['description'] in dups:
            prev = cur
            cur = i['description']
            if prev != cur:
                count += 1
                print()
                print(f'---- {count:>2} ----------------------------------')
            unit = i['attributeUnit'] if 'attributeUnit' in i.keys() else None
            print()
            print(f"Description:      {i['description']}")
            print(f"Unit:             {unit}")
            print(f"Type:             {i['attributeType']}")
            if unit: checkUnit(unit, dimension=True)


def main():
    global ATTRIBUTE_MAP
    global UNIT_LIST_MAP

    banner = r"""
 _   _           _       _          ___  _   _        _ _           _            
| | | |         | |     | |        / _ \| | | |      (_) |         | |           
| | | |_ __   __| | __ _| |_ ___  / /_\ \ |_| |_ _ __ _| |__  _   _| |_ ___  ___ 
| | | | '_ \ / _` |/ _` | __/ _ \ |  _  | __| __| '__| | '_ \| | | | __/ _ \/ __|
| |_| | |_) | (_| | (_| | ||  __/ | | | | |_| |_| |  | | |_) | |_| | ||  __/\__ \
 \___/| .__/ \__,_|\__,_|\__\___| \_| |_/\__|\__|_|  |_|_.__/ \__,_|\__\___||___/
      | |                                                                        
      |_|                                                                        

    """
    print(banner)

    # bootstrap the pickle files to the caches
    try:
        attribute_pickle = 'attributes.pickle'
        with open(attribute_pickle, 'rb') as fin:
            ATTRIBUTE_MAP = pickle.load(fin)
        print(f'Loaded {attribute_pickle} file')
    except:
        print(f'No {attribute_pickle} file')

    try:
        unitlist_pickle = 'unitlist.pickle'
        with open(unitlist_pickle, 'rb') as fin:
            UNIT_LIST_MAP = pickle.load(fin)
        print(f'Loaded {unitlist_pickle} file')
    except:
        print(f'No {unitlist_pickle} file')

    # print("attr map: ", ATTRIBUTE_MAP.keys())
    # print("unit list map: ", UNIT_LIST_MAP.keys())

    print(f'fetching all attributes from {ENDPOINT}...')
    print()
    res = requests.get(BASE_URL + ENDPOINT, cookies=COOKIES, headers=HEADERS)
    skipped = []
    with open(SKIPPED_FILENAME, 'r') as fin:
        for line in fin:
            skipped.append(line.strip())
    if '***USER-NOT-LOGIN***' in res.text:
        print('bad SESSION_ID or token')
        print('try again')
    else:
        # prettyPrint(res)
        parsed = json.loads(res.text)
        # prettyPrint(res)

        # uncomment to generate a report
        ##########################
        # GENERATE A REPORT      #
        ##########################
        print(f"Number of attributes: {len(parsed['data'])}")
        count = 0
        for attribute in parsed['data']:
            if 'attributeValueType' in attribute.keys()\
               and attribute['attributeValueType'] == 'NUMBER'\
               and attribute['attributeType'] in skipped:
                count+=1
                print(f'{count:>2}. {attribute["attributeType"]:.<35} {attribute["description"]}')
        dups = computeDuplicates(parsed['data'])

        # uncomment to process attributes
        ##########################
        # PROCESS THE ATTRIBUTES #
        ##########################
        # for attribute in parsed['data']:
        #     if 'attributeValueType' in attribute.keys()\
        #        and attribute['attributeValueType'] == 'NUMBER'\
        #        and ('attributeUnit' not in attribute.keys()\
        #        or attribute['attributeUnit'] == '')\
        #        and attribute['attributeType'] not in skipped:

        #         if attribute['description'] in ATTRIBUTE_MAP.keys():
        #             pass
        #             original_description = attribute['description']
        #             print(f'Using the cached attribute for {original_description}')
        #             cached_attribute = ATTRIBUTE_MAP[original_description]
        #             if 'attributeUnit' not in cached_attribute.keys():
        #                 base_unit = input(f'Provide a base unit: ')
        #                 cached_attribute['attributeUnit'] = base_unit
        #                 ATTRIBUTE_MAP[original_description] = cached_attribute
        #             if 'attributeUnitList' not in cached_attribute.keys():
        #                 unit_list = input(f'Provide a unit list: ')
        #                 cached_attribute['attributeUnitList'] = unit_list 
        #                 ATTRIBUTE_MAP[original_description] = cached_attribute
        #             if cached_attribute['description'] == None or cached_attribute['description'] == '':
        #                 cached_attribute['description'] = original_description
        #             print(f'Base Unit: {cached_attribute["attributeUnit"]}')
        #             print(f'Unit List: {cached_attribute["attributeUnitList"]}')
        #             updateAttribute(original_description, cached_attribute)
        #         else:
        #             prompt(attribute)

        #         print()
        #         # update the pickle files at each attribute
        #         with open(attribute_pickle, 'wb') as fout:
        #             pickle.dump(ATTRIBUTE_MAP, fout)
        #         with open(unitlist_pickle, 'wb') as fout:
        #             pickle.dump(UNIT_LIST_MAP, fout)

        #         input('Press enter for next attribute')



if __name__ == "__main__":
    main()