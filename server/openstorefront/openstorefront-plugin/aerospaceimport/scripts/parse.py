import xml.etree.ElementTree as ET
import pickle
import copy
import math
import signal
import sys
import csv
import html

# TODO:
# set XML and CSV files relative to the repo

def list_columns(obj, cols=3, columnwise=True, gap=4, numbers=True):
    """
    see https://stackoverflow.com/questions/1524126/how-to-print-a-list-more-nicely
    Print the given list in evenly-spaced columns.

    Parameters
    ----------
    obj : list
        The list to be printed.
    cols : int
        The number of columns in which the list should be printed.
    columnwise : bool, default=True
        If True, the items in the list will be printed column-wise.
        If False the items in the list will be printed row-wise.
    gap : int
        The number of spaces that should separate the longest column
        item/s from the next column. This is the effective spacing
        between columns based on the maximum len() of the list items.
    """

    sobj = [str(item) for item in obj]
    if numbers:
        for i in range(len(sobj)):
            sobj[i] = '{}) '.format(i) + sobj[i]

    if cols > len(sobj): cols = len(sobj)
    max_len = max([len(item) for item in sobj])
    if columnwise: cols = int(math.ceil(float(len(sobj)) / float(cols)))
    plist = [sobj[i: i+cols] for i in range(0, len(sobj), cols)]
    if columnwise:
        if not len(plist[-1]) == cols:
            plist[-1].extend(['']*(len(sobj) - len(plist[-1])))
        plist = zip(*plist)
    printer = '\n'.join([
        ''.join([c.ljust(max_len + gap) for c in p])
        for p in plist])
    return printer

def print_keys(keys):
    user_input = input('Show all existing orgs [yN]: ')
    if user_input == 'y' or user_input == 'Y':
        print('---- ORGANIZATIONS ----')
        print(list_columns(keys))
        print('-----------------------')
        print()

def get_org(keys):
    print_keys(keys)
    user_input = input('Type an org the list above: ')
    if (not user_input.isdigit()) or int(user_input) not in range(len(keys)):
        print('Org not found in list. Try again.')
        return get_org(keys)
    else:
        org = keys[int(user_input)]
        user_input = input('You chose {}. Is this correct? [Yn]'.format(org))
        if user_input == 'n' or user_input == 'N':  
            return get_org(keys)
        else:
            return org

def create_org_prompt(keys):
    short_name = input('Organization short name: ')
    if short_name in keys:
        print('That organization name is already take. Try again or choose an existing organization')
        return create_org_prompt(keys)
    long_name = input('Organization long name: ')
    org = create_new_org(short_name, long_name)
    return org

def create_new_org(short_name, long_name):
    # create new organization
    rel_org      = ET.Element('related_organization')
    _role        = ET.SubElement(rel_org, 'role')
    _org         = ET.SubElement(rel_org, 'organization')
    _key         = ET.SubElement(_org, 'key')
    _short_name  = ET.SubElement(_org, 'short_name')
    _long_name   = ET.SubElement(_org, 'long_name')
    _description = ET.SubElement(_org, 'description')
    _type        = ET.SubElement(_org, 'type')

    _short_name.text = short_name
    _long_name.text = long_name

    return rel_org

previous_org = None
previous_new_org = None

def prompt(org_map, keys, tree):
    try:
        print('What would you like to do?')
        print('\t1) Skip')
        print('\t2) Select an existing organization')
        print('\t3) List existing organizations')
        print('\t4) Create a new organization')
        print('\t5) Use previous new organization')
        print('\t6) Use previous organization assignment')
        print('\t7) Save current state of XML')
        print('\t8) Pickle the organization map')
        user_input = input('>>> ')
        org = None 
        new_org = None
        global previous_org
        global previous_new_org
        if user_input == '1':
            pass
        elif user_input == '2':
            org = get_org(keys)
        elif user_input == '3':
            print_keys(keys)
            return prompt(org_map, keys, tree)
        elif user_input == '4':
            new_org = create_org_prompt(keys)
        elif user_input == '5':
            if previous_new_org:
                new_org = previous_new_org
                print('You have chosen:')
                ET.dump(new_org)
            else:
                print('No previous new organization')
                return prompt(org_map, keys, tree)
        elif user_input == '6':
            if previous_org:
                org = previous_org
                print('You have chosen:')
                print(org)
            else:
                print('No previous organization')
                return prompt(org_map, keys, tree)
        elif user_input == '7':
            filename = 'output.xml'
            user_input = input('Give a filename to output to: [{}] '.format(filename))
            if user_input != '':
                filename = user_input
            tree.write(filename)
            print('XML written to {}'.format(filename))
            return prompt(org_map, keys, tree)
        elif user_input == '8':
            filename = 'orgs.pickle'
            user_input = input('Give a filename to output pickle to: [{}] '.format(filename))
            if user_input != '':
                filename = user_input
            with open(filename, 'wb') as fout:
                pickle.dump(org_map, fout)
            return prompt(org_map, keys, tree)
        else:
            print('not a valid choice')
            return prompt(org_map, keys, tree)
        user_input = input('Redo assignment? [yN] ')
        if user_input == 'y' or user_input == 'Y':
            return prompt(org_map, keys, tree)

        previous_org = org
        previous_new_org = new_org
        # after user selection insert the missing org
        if org and org_map[org]:
            return org_map[org]
        if new_org:
            return [new_org]
    except KeyboardInterrupt:
        print()
        user_input = input('Do you want to exit? [Yn] ')
        if user_input == 'N' or user_input == 'n':
            return prompt(org_map, keys, tree)
        sys.exit(0)



def build_org_map(root):
    # build org map
    org_map = {}
    for product in root:
        orgs = product.find('organizations')
        if len(orgs.getchildren()):
            related_orgs = orgs.getchildren()
            org = related_orgs[0].find('organization')
            short_name = org.find('short_name').text
            long_name = org.find('long_name').text
            if short_name:
                org_map[short_name] = orgs.getchildren()
            elif long_name:
                org_map[long_name] = orgs.getchildren()

    return get_map_keys(org_map)

def build_category_set(root):
    cat_set = set()
    for product in root:
        category_name = product\
            .find('product_revision')\
            .find('product_family')\
            .find('classification')\
            .find('category_name')\
            .text
        cat_set.add(category_name)
    return cat_set

def print_missing_mappings():
    filename = r"\\hera\C4ISR_NSS\SPOON\projects\aerospace-import\preprocessor\products_091118-preprocessed.xml"
    print(f'XML file: {filename}')
    tree = ET.parse(filename)
    root = tree.getroot()
    category_set = build_category_set(root)

    csv_filename = r"C:\dev\openstorefront\server\openstorefront\openstorefront-plugin\aerospaceimport\src\main\resources\componentTypeMapping.csv"
    print(f'Comparing with: {csv_filename}')
    csv_set = set()
    with open(csv_filename) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count != 0:
                csv_set.add(row[0])
            line_count += 1
    print('Set difference between mapping and XML file: ', csv_set.difference(category_set))
    print()


def update_org_map(org_map, new_org):
    key = new_org[0].find('organization').find('short_name').text
    if key:
        org_map[key] = new_org

    return get_map_keys(org_map)

def get_map_keys(org_map):
    keys = list(org_map.keys())
    keys.sort()
    return (org_map, keys)

def process_xml():
    filename = 'products_091118.xml'
    user_input = input('What XML file would you like to use? [{}] '.format(filename))
    if user_input != '':
        filename = user_input
    tree = ET.parse(filename)
    root = tree.getroot()

    user_input = input('Add orgs from pickle file? [yN] ')
    org_map, keys = build_org_map(root)
    if user_input == 'Y' or user_input == 'y':
        picklefile = 'orgs.pickle'
        user_input = input('Provide a pickle file: [{}] '.format(picklefile))
        if user_input != '':
            picklefile = user_input
        with open(picklefile, 'rb') as fin:
            orgs_pickle = pickle.load(fin)
            new_org_map, keys = get_map_keys(orgs_pickle)
        # add new_org_map values to the org_map
        # new_org_map values overwrite org_map values
        org_map = {**org_map, **new_org_map}


    for product in root:
        orgs = product.find('organizations')
        if len(orgs.getchildren()) == 0:
            print()
            print('--- NO ORG FOUND ---')
            print('SHORT_NAME: ', product.find('short_name').text)
            print('LONG_NAME: ', product.find('long_name').text)
            print('KEY: ', product.find('key').text)
            print('DESC: ')
            print(product.find('description').text)

            revision = product.find('product_revision')

            default_org = ''
            if revision and revision.find('provenance'):
                print()
                print('--- PROVENANCE DOCS ---')
                for item in revision.find('provenance').getchildren():
                    print('TITLE: ', item.find('title').text)
                    print('DESC: ', item.find('description').text)
                    if item.find('filename'):
                        print('FILENAME: ', item.find('filename').text)

            print('----------------------')
            print()

            # prompt user for org name
            new_org = prompt(org_map, keys, tree)
            if new_org:
                print('Added organization to the product.')
                org_map, keys = update_org_map(org_map, new_org)
                print('XML dump:')
                for i in new_org:
                    orgs.append(i)
                ET.dump(orgs)

    OUTPUT_FILENAME = 'final.xml'
    print('Writing the new XML to {}'.format(OUTPUT_FILENAME))
    tree.write(OUTPUT_FILENAME)

def inspect_escape_characters():
    filename = r"\\hera\C4ISR_NSS\SPOON\projects\aerospace-import\preprocessor\products_091118-preprocessed_escaped.xml"
    print(f'XML file: {filename}')
    tree = ET.parse(filename)
    root = tree.getroot()
    all_descriptions = []
    count = 0
    html_style = """
    <style>
table, th, td {
  border: 1px solid black;
  border-collapse: collapse;
}
th, td {
  padding: 15px;
}
    </style>
    """
    html_string = f'<html><head>{html_style}</head><body><table><tr><th>encoding</th><th>character</th><th>text body</th><tr>'
    for product in root:
        descriptions = product.findall('description')
        all_descriptions += descriptions
    for description in all_descriptions:
        text = description.text
        if text:
            encoded = text.encode('ascii', 'xmlcharrefreplace')
            words = encoded.replace(b'&#160;', b' ').split()
            uwords = text.split()
            for i in range(len(words)):
                if b'&#' in words[i] or b'?' in words[i]:
                    count += 1
                    print('----------------------------------------------------------------------')
                    print()
                    print(f'Escape character: {words[i].decode("utf-8")} -> {uwords[i]}')
                    html_string += f'<tr><td>{html.escape(words[i].decode("utf-8"))}</td><td>{uwords[i]}</td><td>{text}</td></tr>'
                    print()
                    # print()
                    # print('DESCRIPTION TEXT')
                    # print('vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv')
                    # print(text)
                    # print('^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^')
                    print('----------------------------------------------------------------------')
    print()
    print(f'Found {count} escaped characters')
    print()
    html_string += '</table></body></html>'
    with open('index.html', 'w') as fout:
        fout.write(html_string)

def intro_prompt():
    print('What would you like to do?')
    print('\t1. Clean XML')
    print('\t2. Check CSV entry mappings')
    print('\t3. Inspect escape characters')
    print('\t4. Quit')
    user_input = input('>>> ')
    if user_input == '1':
        process_xml()
        return
    elif user_input == '2':
        print_missing_mappings()
        return intro_prompt()
    elif user_input == '3':
        inspect_escape_characters()
        return intro_prompt()
    elif user_input == '4':
        return
    else:
        print('invalid choice')
        return intro_prompt()


if __name__ == "__main__":
    intro_text = r"""
   ___                                                                          
  / _ |___ _______                                                              
 / __ / -_) __/ _ \                                                             
/_/ |_\__/_/_ \___/                                                             
     / ___/ /__ ___ ____  __ _____                                              
    / /__/ / -_) _ `/ _ \/ // / _ \                                             
    \___/_/\__/\_,_/_//_/\_,_/ .__/                                             
         ____        _      /_/                                                 
        / __/_______(_)__  / /_                                                 
       _\ \/ __/ __/ / _ \/ __/                                                 
      /___/\__/_/ /_/ .__/\__/                                                  
                   /_/                                                          

    """
    print(intro_text)
    intro_prompt()
