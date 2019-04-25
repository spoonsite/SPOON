import requests
import json
import sys
import pickle
from common_functions import computeDuplicates, checkUnit, checkUnitList, COOKIES, HEADERS, BASE_URL, ENDPOINT

def cherrypick(dups, attributes):
    typecodes = {}
    units = {}
    opRestrictions = {}
    reqRestrictions = {}
    attributeValueType = ''

    for attribute in attributes:
        try:
            attributeValueType = attribute['attributeValueType']
        except:
            attributeValueType = ''
        if attribute['description'] in dups and attributeValueType == 'NUMBER':
            # set up dictionaries
            try: 
                typecodes[attribute['description']].append(attribute['attributeType'])
            except:
                typecodes[attribute['description']] = [attribute['attributeType']]
            try:
                units[attribute['attributeType']] = attribute['attributeUnit']
            except:
                units[attribute['attributeType']] = ''
            
            # get default lists of restrictions
            if 'optionalRestrictions' in attribute.keys():
                if not attribute['description'] in opRestrictions:
                    opRestrictions[attribute['description']] = attribute['optionalRestrictions']
            if 'requiredRestrictions' in attribute.keys():
                if not attribute['description'] in reqRestrictions:
                    reqRestrictions[attribute['description']] = attribute['requiredRestrictions']

    # print(typecodes)
    for desc in typecodes:
        acceptedInput = False
        while not acceptedInput:
            deleteList = []
            count = 0
            currCodes = typecodes[desc]
            for code in currCodes:
                count = count + 1
                print(f'{count}. {code} ({units[code]})')

            # get information for deletelist
            merging = input("Which attributes do you want to merge? (ex: 1,2,4) '0' to exit ")
            if merging == '0':
                break
            currentmerging = merging.split(",")
            print(currentmerging)
            for num in currentmerging:
                try:
                    deleteList.append(currCodes[int(num)-1])
                except:
                    pass
            print(f"Parts to be deleted: {deleteList}")

            # get information for new attributetypesave
            description = input("Enter a description: ")
            attributeType = input("Enter an attributeType: ")
            attributeType.upper()
            detailedDescription = input("Enter a detailed description: ")
            attributeUnit = input("Enter an attribute unit: ")
            checkUnit(attributeUnit)
            attributeUnitList = input("Enter an attribute unit list (comma seperated ex. 'm,cm,mm'): ")
            attributeUnitList = attributeUnitList.split(",")
            checkUnitList(attributeUnit, attributeUnitList)
            
            # ensures the saved attribute does not get deleted
            deleteList = [x for x in deleteList if not x == attributeType]

            # creating the object to be sent in the api request
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
                    "requiredComponentType": reqRestrictions[desc],
                    "optionalComponentTypes": opRestrictions[desc]
                },
                'attributesTypesToBeDeleted': deleteList
            }
            print(json.dumps(data, indent=4))

            isGood = input('Does this look good? [yn] ')
            if isGood == 'y' or isGood == 'Y':
                acceptedInput = True
                print('Posting...')
                res = requests.post(f'{BASE_URL}/api/v1/resource/attributes/listmergeattributetypes', data=json.dumps(data), cookies=COOKIES, headers=HEADERS)
                print(res)
                CHERRY_MERGED_UNITS.append(data)
                with open(CHERRY_PICKLE, 'wb') as fout:
                    pickle.dump(CHERRY_MERGED_UNITS, fout)
                break


# Use data from pickle file to rerun all of the requests originally done by hand
def usePickledActions():
    try:
        # print(CHERRY_MERGED_UNITS)
        count = 0
        for action in CHERRY_MERGED_UNITS:
            count = count+1
            print(f'Posting via pickle file data... {count}')
            res = requests.post(f'{BASE_URL}/api/v1/resource/attributes/listmergeattributetypes', data=json.dumps(action), cookies=COOKIES, headers=HEADERS)
            print(res)
    except:
        pass

def main():
    global CHERRY_PICKLE
    global CHERRY_MERGED_UNITS

    banner = """
  ___  _   _  ____  ____  ____  _  _    ____  ____  ___  _  _    __  __  ____  ____   ___  ____ 
 / __)( )_( )( ___)(  _ \(  _ \( \/ )  (  _ \(_  _)/ __)( )/ )  (  \/  )( ___)(  _ \ / __)( ___)
( (__  ) _ (  )__)  )   / )   / \  /    )___/ _)(_( (__  )  (    )    (  )__)  )   /( (_-. )__) 
 \___)(_) (_)(____)(_)\_)(_)\_) (__)   (__)  (____)\___)(_)\_)  (_/\/\_)(____)(_)\_) \___/(____)


    """
    print(banner)

    # Load previously performed actions
    CHERRY_PICKLE = 'cherrypick.pickle'
    CHERRY_MERGED_UNITS = []
    try:
        with open(CHERRY_PICKLE, 'rb') as fin:
            CHERRY_MERGED_UNITS = pickle.load(fin)
        print(f'Loaded {CHERRY_PICKLE} file')
    except:
        print(f'No {CHERRY_PICKLE} file')

    # Use the pickled actions
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
        print(f'------------Duplicates - {len(dups)}---------------')
        print(dups)
        print('-------------------------------------')
        print()
        cherrypick(dups, parsed['data'])

if __name__ == "__main__":
    main()