import requests
import json
import sys
import pickle
from common_functions import computeDuplicates, checkUnit, checkUnitList, TOKEN, SESSION_ID

"""
This script will get duplicate attributes and then accept user
input to create a new attribute in its place, which will hold 
all of the codes from all the old attributes. 

This script can only parse items that are both numbers or both text. 
They cannot have different types or it could break the system. 


Adding WSL to VS Code: "terminal.integrated.shell.windows": "C:\\WINDOWS\\sysnative\\bash.exe"
"""

# SESSION_ID = ''
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

SKIPPED_ATTR_FILENAME = 'skipped_attributes.txt'
UNIT_LIST_MAP = {}
ATTRIBUTE_MAP = {}

# return if two units are compatible
def checkUnitPassFail(unit, unit_list):
    data = {
        'baseUnit': unit,
        'units': unit_list
    }
    res = requests.post(BASE_URL + '/api/v1/resource/attributes/unitlistcheck', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    try:
        parsed = json.loads(res.text)['dimension']
        return True
    except:
        pass
    return False

######################### merge duplicates duplicates #########################
def askToMerge(dups, attributes):
    print(f'Found {len(dups)} duplicates')
    val = ''
    # typeCodes: key == Description, value == List TYPE_CODE
    typeCodes = {}
    units = {}
    listOfCompatible=[]
    defaults = {}
    default = {
        'type': '',
        'detailedDescription':'',
        "attributeUnit": '', 
        "attributeUnitList": [],
        "optionalRestrictions": [],
        "requiredRestrictions": []
    }
    actions = []

    # set up dictionary for all duplicate descriptions
    for i in dups:
        defaults[i] = {
            'type': '',
            'detailedDescription':'',
            "attributeUnit": '', 
            "attributeUnitList": [],
            "optionalRestrictions": [],
            "requiredRestrictions": []
        }

    
    # This checks to see if there are any old values that can be used
    # in the new attribute.
    for i in attributes:
        try:
            val = i['attributeValueType']
        except:
            val = ''
        if i['description'] in dups and val == 'NUMBER':
            if defaults[i['description']]['type'] == '':
                defaults[i['description']]['type'] = i['attributeType']

            if defaults[i['description']]['detailedDescription'] == '' and 'detailedDescription' in i.keys():
                defaults[i['description']]['detailedDescription'] = i['detailedDescription']
            
            if defaults[i['description']]['attributeUnit'] == '' and 'attributeUnit' in i.keys():
                defaults[i['description']]['attributeUnit'] = i['attributeUnit']
            
            if defaults[i['description']]['requiredRestrictions'] == [] and 'requiredRestrictions' in i.keys():
                defaults[i['description']]['requiredRestrictions'] = i['requiredRestrictions']
            
            if defaults[i['description']]['optionalRestrictions'] == [] and 'optionalRestrictions' in i.keys():
                defaults[i['description']]['optionalRestrictions'] = i['optionalRestrictions']
            
            if defaults[i['description']]['attributeUnitList'] == [] and 'attributeUnitList' in i.keys():
                defaults[i['description']]['attributeUnitList'] = i['attributeUnitList']
            
            # create mapping for descriptions to typecodes
            try: 
                typeCodes[i['description']].append(i['attributeType'])
            except:
                typeCodes[i['description']] = [i['attributeType']]
            
            # create maping for typecodes to units
            try:
                units[i['attributeType']] = i['attributeUnit']
            except:
                units[i['attributeType']] = ''



    not_skipped = filter(lambda x: x not in SKIPPED_ATTR, typeCodes.keys())
    for desc in not_skipped:
        if len(typeCodes[desc]) > 1: # && checkUnitPassFail(units[desc], [all other base units])
            current = {
                'type': '',
                'detailedDescription':'',
                "attributeUnit": '', 
                "attributeUnitList": [],
                "optionalRestrictions": [],
                "requiredRestrictions": []
            }

            if desc in defaults:
                current = defaults[desc]
            else: 
                print('could not find default')

            goodInput = False

            # TODO: check that all attribute attempting to be merged have compatible units
            #       see /unitchecklist endpoint
            #       POST { baseUnit: 'g', units: ['g','mg','kg']}
            answer = input(f"Do you want to merge the attributes with the name {desc}? (Y/N) ")
            while not goodInput:
                if answer == 'y' or answer == 'Y':
                    description = input(f"Enter a description, leave blank for \'{desc}\' ")
                    if len(description) == 0:
                        description = desc
                    attributeType = input(f"Enter an attribute type, leave blank for \'{current['type']}\' ")
                    if len(attributeType) == 0:
                        attributeType = current['type']
                    attributeType=attributeType.upper()
                    detailedDescription = input(f"Enter a detailed description, leave blank for \'{current['detailedDescription']}\' ")
                    if len(detailedDescription) == 0:
                        detailedDescription = current['detailedDescription']
                    attributeUnit = input(f"Enter an attribute unit, leave blank for \'{current['attributeUnit']}\' ")
                    if len(attributeUnit) == 0:
                        attributeUnit = current['attributeUnit']
                    checkUnit(attributeUnit)
                    attributeUnitList = []
                    tempUnit = ""
                    while True:
                        tempUnit = input(f"Enter a compatible unit (\'exit\' to end), leave blank for \'{current['attributeUnitList']}\' ")
                        if tempUnit == '':
                            attributeUnitList = current['attributeUnitList']
                            break
                        elif tempUnit != "exit":
                            attributeUnitList.append(tempUnit)
                        else:
                            break
                    if attributeUnitList == ['']:
                        attributeUnitList = []
                    print(attributeUnitList)
                    checkUnitList(attributeUnit, attributeUnitList)

                    # checks if units from attributes with the same description are compatible
                    listOfCompatible=[]
                    deleteList=[]
                    for code in typeCodes[desc]:
                        if checkUnitPassFail(attributeUnit, [units[code]]):
                            listOfCompatible.append(code)

                    deleteList = [x for x in listOfCompatible if not x == attributeType]
                    print(f"Attributes to be deleted: {deleteList}")

                    temp = input('Press enter to continue (or M. Type \'exit\' to leave loop) ...')
                    if temp == 'm' or temp == 'M':
                        deleteList=[]
                        inputVar = ''
                        while True:
                            inputVar = input('attribute to be deleted ')
                            if inputVar == 'exit':
                                break
                            deleteList.append(inputVar)

                    data = {
                        'attributeTypeSave': {
                            "attributeType":{
                                "description":f"{description}",
                                "attributeType":f"{attributeType}",
                                "defaultAttributeCode":"",
                                "detailedDescription":f"<p>{detailedDescription}</p>",
                                "attributeValueType":"NUMBER",
                                "attributeUnit":f"{attributeUnit}",
                                "attributeUnitList": attributeUnitList,
                                "allowMultipleFlg":"true",
                                "allowUserGeneratedCodes":"true"
                            },
                            "requiredComponentType": current['requiredRestrictions'],
                            "optionalComponentTypes": current['optionalRestrictions']
                        },
                        'attributesTypesToBeDeleted': deleteList
                    }
                    print(json.dumps(data, indent=4))
                    # post to endpoint
                    tempGoodInput = input("Does this new attribute look right? (Y/N) ")

                    # sends data to server
                    if tempGoodInput == 'Y' or tempGoodInput == 'y':
                        goodInput = True
                        print('Posting...')
                        res = requests.post(f'{BASE_URL}/api/v1/resource/attributes/listmergeattributetypes', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
                        print(res)
                        MERGED_UNITS.append(data)
                        with open(MERGED_PICKLE, 'wb') as fout:
                            pickle.dump(MERGED_UNITS, fout)
                        break
                elif answer == 'N' or answer == 'n':
                    with open(SKIPPED_ATTR_FILENAME, 'a') as fout:
                        fout.write(desc + '\n')
                    break
    

def usePickledActions():
    try:
        # print(MERGED_UNITS)
        count = 0
        for action in MERGED_UNITS:
            count = count+1
            print(f'Posting via pickle file data... {count}')
            res = requests.post(f'{BASE_URL}/api/v1/resource/attributes/listmergeattributetypes', data=json.dumps(action), cookies=COOKIES, headers=HEADERS)
            print(res)
    except:
        pass

def main():
    global MERGED_UNITS
    global MERGED_PICKLE
    global SKIPPED_ATTR

    banner = r"""
                                                       _               
 _ __ ___   ___ _ __ __ _  ___   _ __  _   _ _ __ ___ | |__   ___ _ __ 
| '_ ` _ \ / _ \ '__/ _` |/ _ \ | '_ \| | | | '_ ` _ \| '_ \ / _ \ '__|
| | | | | |  __/ | | (_| |  __/ | | | | |_| | | | | | | |_) |  __/ |   
|_| |_| |_|\___|_|  \__, |\___| |_| |_|\__,_|_| |_| |_|_.__/ \___|_|   
                    |___/                                              
             _ _       
 _   _ _ __ (_) |_ ___ 
| | | | '_ \| | __/ __|
| |_| | | | | | |_\__ \
 \__,_|_| |_|_|\__|___/
                       
    """
    print(banner)

    # Load previously performed actions
    MERGED_PICKLE = 'merged.pickle'
    MERGED_UNITS =[]
    try:
        with open(MERGED_PICKLE, 'rb') as fin:
            MERGED_UNITS = pickle.load(fin)
        print(f'Loaded {MERGED_PICKLE} file')
    except:
        print(f'No {MERGED_PICKLE} file')

    # Load previously skipped attribute groupings
    SKIPPED_ATTR = []
    try:
        with open(SKIPPED_ATTR_FILENAME, 'r') as fin:
            for line in fin:
                SKIPPED_ATTR.append(line.strip())
    except:
        print(f'No {SKIPPED_ATTR_FILENAME} file')
    
    usePickledActions()

    print(f'fetching all attributes from {ENDPOINT}...')
    print()

    res = requests.get(BASE_URL + ENDPOINT, cookies=COOKIES, headers=HEADERS)

    if '***USER-NOT-LOGIN***' in res.text:
        print('bad SESSION_ID or token')
        print('try again')
    else:
        parsed = json.loads(res.text)
        # prettyPrint(res)

        dups = computeDuplicates(parsed['data'])
        print('------------Duplicates---------------')
        print(dups)
        print('-------------------------------------')
        print()

        askToMerge(dups, parsed['data'])

if __name__ == "__main__":
    main()