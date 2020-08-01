# Custom Object Detection

## Model training Steps, Tensorflow: 1.15


### Steps

1) Get data from source.
2) Activate env: __sih3__.
3) Execute `scripts/rename_transfer_remove.py`.

```python
python scripts/rename_transfer_remove.py
```

4) Check Orientation of images using:

```python 
python scripts/orientation_correction.py -i <image_dir>
```

5) Resize images and corresponding xmls using `resiz` env and scripts in __scripes/resize_tool__ folder.

```python
conda activate resiz
python scripts/resize_tool/main.py -p <IMAGE&ANNOT dir> -o <RESIZED_OUTPUT_DIR> -x <NEW_X> -y <NEW_Y> -s <1 or 0>
``` 

6) Execute **Image augmentations.ipynb** to generate augmented images and xmls also generating __labels_{IM_SIZE}.csv__ for different image sizes .
7) Generate training and validation splits `split_dataset.py`. Ratio: [90-10].

```python
    python scripts/split_dataset.py -i annotations\labels_{IM_SIZE}\.csv -o annotations\ -r 0.1
```

8) Create a folder, inside it create a **Images_{IM_SIZE}** folder containing both all images and xmls for that size, a folder of **annotations** conataning .csv files and **generate_tfrecord.py** file.
9)  Create a zip file of the created folder for colab.
10) Use the `TF_1_Custom_Object_detection.ipynb` notebook for training and conversion of chosen model.