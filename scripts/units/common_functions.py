import requests
import json
import sys

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

########################## Hit Ryan's endpoint to see if valid unit ##########################
def checkUnit(unit, dimension=False):
    data = {
        'unit': unit
    }
    res = requests.post(BASE_URL + '/api/v1/resource/attributes/unitcheck', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    try:
        parsed = json.loads(res.text)
        if dimension:
            print(f"Dimension:        {parsed['dimension']}")
        else:
            print('<---------- Unit check ---------->')
            print(f"Unit:          {parsed['unit']}")
            print(f"Dimension:     {parsed['dimension']}")
            print(f"SI Unit:       {parsed['standardUnit']}")
    except:
        print('Something went wrong')
        print(res.text)

##########################Hit Ryan's endpoint to see if valid unit list##########################
def checkUnitList(unit, unit_list):
    data = {
        'baseUnit': unit,
        'units': unit_list
    }
    res = requests.post(BASE_URL + '/api/v1/resource/attributes/unitlistcheck', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
    try:
        parsed = json.loads(res.text)
        print('<---------- Unit List Check ---------->')
        print(f"Dimension:        {parsed['dimension']}")
        print(f"SI Unit:       {parsed['standardUnit']}")
    except:
        print('Something went wrong')
        print(res.text)

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

######################### find duplicates #########################
def computeDuplicates(attributes):
    seen = {}
    attr = {} 
    dupes = []

    for attribute in attributes:
        des = attribute['description']
        if des not in seen:
            seen[des] = 1
        else:
            if seen[des] == 1:
                dupes.append(des)
            seen[des] += 1
    return dupes