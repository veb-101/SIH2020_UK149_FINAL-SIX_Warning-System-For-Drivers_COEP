import os
import shutil
import os.path as P
BASE_FOLDER = 'kaggle_dataset'
DST_FOLDER = 'kaggle_renamed'

if P.isdir(DST_FOLDER):
    shutil.rmtree(DST_FOLDER)

os.makedirs(DST_FOLDER)


xml_files = [P.join(BASE_FOLDER, file)
             for file in os.listdir(BASE_FOLDER) if file.endswith('.xml')]

missing = []

img_ext = '.jpg'

for i, file in enumerate(xml_files, 1):
    path, filename = file.split('\\')
    xml_file_name, xml_ext = P.splitext(filename)

    src_xml_name = f'{xml_file_name}{xml_ext}'
    src_img_name = f'{xml_file_name}{img_ext}'

    if not os.path.isfile(P.join(path, src_img_name)):
        missing.append(path)
        continue

    dst_xml_name = f'kaggle_{i:04}{xml_ext}'
    dst_img_name = f'kaggle_{i:04}{img_ext}'

    shutil.copy(file, P.join(DST_FOLDER, dst_xml_name))  # XML

    shutil.copy(P.join(path, src_img_name),
                P.join(DST_FOLDER, dst_img_name))
    print(f"\rProcessed: {i} files", end='')
