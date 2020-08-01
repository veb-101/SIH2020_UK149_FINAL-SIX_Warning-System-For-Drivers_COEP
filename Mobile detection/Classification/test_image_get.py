import os
import shutil
import pandas as pd

data = pd.read_csv('Test.csv')
SRC_DIR = r'D:\desktop\code-works\Deep_learning_and_machine_learning\traffic_signs\traffic-sign-recognition\gtsrb-german-traffic-sign-Copy'
DST_DIR = r'D:\desktop\code-works\Deep_learning_and_machine_learning\traffic_signs\traffic-sign-recognition\gtsrb-german-traffic-sign\Test'
i = 0
for idx, row in data.iterrows():

    img_path_exists = os.path.exists(os.path.join(
        DST_DIR, rf'{row["Path"]}'))
    if img_path_exists:
        print(row['Path'])
    # img_file = os.path.join(SRC_DIR, row['Path'])
    # shutil.copy(img_file, DST_DIR)
    # print(f"\r{i}", end='')
    # i += 1
print()
# print(row['Path'], ":", img_path_exists)
