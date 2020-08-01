# Custom Object Detection

## Model: MobileDet, Tensorflow: 1.15

### Steps

1) Copy data from source.
2) copy files from script to root directory
3) Activate env: __sih3__.
4) Execute `scripts/rename_transfer_remove.py`.

```python
python scripts/rename_transfer_remove.py
```

5) Resize images using `resiz` env and scripts in __resize_tool__ folder.

```python
conda activate resiz
python resize_tool/main.py -p <IMAGE&ANNOT dir> -o <RESIZED_OUTPUT_DIR> -x <NEW_X> -y <NEW_Y> -s <1 or 0>
``` 

6) Execute Image augmentations which will generate __labels.csv__ and __all_labels.csv__.
7) Generate training and validation splits `split_dataset.py`. Ratio: [90-10].

```python
python scripts/split_dataset.py -og annotations\labels.csv -m annotations\all_labels.csv -r 0.1 -o annotations
```

8) Move __*.csv__ files in annotations folder.
9) Create a folder, inside it create a **images** folder containing both augmented and orignal images, a folder of **annotations** conataning .csv files and **generate_tfrecord.py** file.
10) Create a zip file of the created folder for colab.
11) Use the `TF_1_Custom_Object_detection.ipynb` notebook for training and conversion of chosen model.