# based on https://github.com/tensorflow/examples/blob/master/lite/examples/object_detection/raspberry_pi/detect_picamera.py
from tflite_runtime.interpreter import Interpreter, load_delegate
import argparse
import time
import cv2
import re
import numpy as np
# from multiprocessing import Pipe
# import multiprocessing


def draw_image(image, results, labels, size):
    # result_size = len(results)
    for idx, obj in enumerate(results):
        # Prepare image for drawing
        # draw = ImageDraw.Draw(Image.fromarray(image))
        if labels[obj['class_id']] in ("car", "bus", "person"):
            # Prepare boundary box
            ymin, xmin, ymax, xmax = obj["bounding_box"]

            xmin = int(max(1, xmin * size[0]))
            xmax = int(min(size[0], xmax * size[0]))
            ymin = int(max(1, ymin * size[1]))
            ymax = int(min(size[1], ymax * size[1]))

            # Draw rectangle to desired thickness
            label = f"{labels[obj['class_id']]}: {round(obj['score'] * 100, 2)}%"
            print(label)
            # break
            labelSize, baseLine = cv2.getTextSize(
                label, cv2.FONT_HERSHEY_SIMPLEX, 0.7, 2
            )

            label_ymin = max(
                ymin, labelSize[1] + 10
            )
            cv2.rectangle(
                image,
                (xmin, label_ymin - labelSize[1] - 10),
                (xmin + labelSize[0], label_ymin + baseLine - 10),
                (255, 255, 255),
                cv2.FILLED,
            )  # Draw white box to put label text in
            cv2.putText(
                image,
                label,
                (xmin, label_ymin - 7),
                cv2.FONT_HERSHEY_SIMPLEX,
                0.7,
                (0, 0, 0),
                2,
            )
        cv2.imshow("Live Object Detection", image)


def load_labels(path):
    """Loads the labels file. Supports files with or without index numbers."""
    with open(path, "r", encoding="utf-8") as f:
        lines = f.readlines()
        labels = {}
        for row_number, content in enumerate(lines):
            pair = re.split(r"[:\s]+", content.strip(), maxsplit=1)
            if len(pair) == 2 and pair[0].strip().isdigit():
                labels[int(pair[0])] = pair[1].strip()
            else:
                labels[row_number] = pair[0].strip()
    # print(labels)
    return labels


def set_input_tensor(interpreter, image):
    """Sets the input tensor."""
    tensor_index = interpreter.get_input_details()[0]["index"]
    input_tensor = interpreter.tensor(tensor_index)()[0]
    input_tensor[:, :] = image


def get_output_tensor(interpreter, index):
    """Returns the output tensor at the given index."""
    output_details = interpreter.get_output_details()[index]
    tensor = np.squeeze(interpreter.get_tensor(output_details["index"]))
    return tensor


def detect_objects(interpreter, image, threshold):
    """Returns a list of detection results, each a
    dictionary of object info."""
    set_input_tensor(interpreter, image)
    interpreter.invoke()

    # Get all output details
    boxes = get_output_tensor(interpreter, 0)
    classes = get_output_tensor(interpreter, 1)
    scores = get_output_tensor(interpreter, 2)
    count = int(get_output_tensor(interpreter, 3))

    results = []
    for i in range(count):
        if scores[i] >= threshold:
            result = {
                "bounding_box": boxes[i],
                "class_id": classes[i],
                "score": scores[i],
            }
            results.append(result)
    return results


def make_interpreter(model_file, use_edgetpu):
    model_file, *device = model_file.split("@")
    if use_edgetpu:
        return Interpreter(
            model_path=model_file,
            experimental_delegates=[
                load_delegate(
                    "libedgetpu.so.1", {"device": device[0]} if device else {}
                )
            ],
        )
    else:
        return Interpreter(model_path=model_file)


def main():
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    parser.add_argument(
        "-m", "--model", type=str, required=True,
        help="File path of .tflite file."
    )
    parser.add_argument(
        "-l", "--labels", type=str, required=True,
        help="File path of labels file."
    )
    parser.add_argument(
        "-t",
        "--threshold",
        type=float,
        default=0.4,
        required=False,
        help="Score threshold for detected objects.",
    )
    parser.add_argument("-v", "--video", type=str,
                        required=True, help="Path to video")
    parser.add_argument(
        "-e", "--use_edgetpu", action="store_true", default=False, help="Use EdgeTPU"
    )
    args = parser.parse_args()

    labels = load_labels(args.labels)
    interpreter = make_interpreter(args.model, args.use_edgetpu)
    interpreter.allocate_tensors()
    _, input_height, input_width, _ = interpreter.get_input_details()[
        0]["shape"]

    # Initialize video stream
    cap = cv2.VideoCapture(args.video)
    time.sleep(1)

    while cap.isOpened():
        try:
            ret, frame = cap.read()
            frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            frame_resized = cv2.resize(frame_rgb, (input_width, input_height))
            input_data = np.expand_dims(frame_resized, axis=0)
            # Perform inference
            results = detect_objects(interpreter, input_data, args.threshold)
            # print(results)
            width = cap.get(3)  # float
            height = cap.get(4)  # float

            # print('width, height:', width, height)
            # print(frame.shape)
            # break
            draw_image(frame, results, labels, (width, height))
            # break
            if cv2.waitKey(5) & 0xFF == ord("q"):
                # fps.stop()
                break
        except KeyboardInterrupt:
            break

    cv2.destroyAllWindows()
    cap.release()
    time.sleep(2)


if __name__ == "__main__":
    main()
