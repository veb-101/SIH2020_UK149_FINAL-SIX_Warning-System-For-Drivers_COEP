import os
import shutil
import cv2

folders = ['01', "02", "03", "04", "05", "06", "07", "08"]
# "not_flood1", "not_flood2", "not_flood3"]
# folders = ["03"]
all_files = [os.path.join(folder, file) for folder in folders for file in os.listdir(folder)
             if file.endswith('.jpg')]

print(len(all_files))

f = ord('f')
n = ord('n')


flood_count = 0
not_flood_count = 0
for count, image_path in enumerate(all_files, 1):
    image = cv2.imread(image_path)
    # imH, imW, _ = image.shapes
    image_resized = cv2.resize(image, (480, 480))
    cv2.imshow("Images annotate", image_resized)
    # k = chr(cv2.waitKey(100))
    k = cv2.waitKey(0)
    if k == f:
        cv2.imwrite(f"flood/{flood_count:03}.jpg", image_resized)
        print(f"{image_path} moved to: flood/{flood_count:03}.jpg")
        flood_count += 1
    elif k == n:
        cv2.imwrite(f"not_flood/{not_flood_count:03}.jpg", image_resized)
        print(f"{image_path} moved to: not_flood/{not_flood_count:03}.jpg")
        not_flood_count += 1
    elif k == 27:  # escape key
        break
    else:
        print(f"skipping: {image_path}")

cv2.destroyAllWindows()
