import requests
import json
import sys
from common_functions import getUnit, getUnitList, computeDuplicates, checkUnit, checkUnitList

"""
This script will get duplicate attributes and then accept user
input to create a new attribute in its place, which will hold 
all of the codes from all the old attributes. 

This script can only parse items that are both numbers or both text. 
They cannot have different types or it could break the system. 
"""

SESSION_ID = '1748FC3E7D9F377904991BC7833D0E6E'
TOKEN = ''

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

######################### merge duplicates duplicates #########################
def askToMerge(dups, attributes):
    print(f'Found {len(dups)} duplicates')
    prev = ''
    cur  = ''
    val = ''
    count = 0
    dict = {}
    defaults = {}
    for i in attributes:
        try:
            val = i['attributeValueType']
        except:
            val = ''
        if i['description'] in dups and val == 'NUMBER':
            print(val)
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
            try:
                defaults[i['description']] = {
                    'type': i['attributeType'],
                    "detailedDescription": i['detailedDescription'],
                    "optionalRestrictions": i['optionalRestrictions'],
                    "requiredRestrictions": i['requiredRestrictions']
                }
            except:
                print('this')

            try: 
                dict[i['description']].append(i['attributeType'])
            except:
                dict[i['description']] = [i['attributeType']]
    # print(dict)
    # for i in dict:
    #     print(dict[i])
    for i in dict:
        if len(dict[i]) > 1:
            current = {
                    'type': '',
                    "detailedDescription": '',
                    "optionalRestrictions": [],
                    "requiredRestrictions": []
            }
            if i in defaults:
                current = defaults[i]
            else: 
                print('could not find default')
            goodInput = False
            answer = input(f"Do you want to merge the attributes with the name {i}? (Y/N) ")
            while not goodInput:
                if answer == 'y' or answer == 'Y':
                    description = input(f"Enter a description, leave blank for \'{i}\' ")
                    if len(description) == 0:
                        description = i
                    attributeType = input(f"Enter an attribute type, leave blank for \'{current['type']}\' ")
                    if len(attributeType) == 0:
                        attributeType = current['type']
                    attributeType=attributeType.upper()
                    detailedDescription = input(f"Enter a detailed description, leave blank for \'{current['detailedDescription']}\' ")
                    if len(detailedDescription) == 0:
                        detailedDescription = current['detailedDescription']
                    attributeUnit = input("Enter an attribute unit ")
                    checkUnit(attributeUnit)
                    attributeUnitList = []
                    tempUnit = ""
                    while True:
                        tempUnit = input(f"Enter a compatible unit (\'exit\' to end) ")
                        if tempUnit != "exit":
                            attributeUnitList.append(tempUnit)
                        else:
                            break
                    checkUnitList(attributeUnit, attributeUnitList)
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
                        'attributesTypesToBeDeleted': dict[i]
                    }
                    print(json.dumps(data))
                    # post to endpoint
                    tempGoodInput = input("Does this new attribute look right? (Y/N) ")
                    if tempGoodInput == 'Y' or tempGoodInput == 'y':
                        goodInput = True

                        res = requests.post(f'{BASE_URL}/api/v1/resource/attributes/listmergeattributetypes', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
                        print(res)
                        # hit endpoint
                        break
                else:
                    break

def main():
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
        print('---------------------------')
        print(dups)
        print('---------------------------')
        print()
        print()
        askToMerge(dups, parsed['data'])

if __name__ == "__main__":
    main()