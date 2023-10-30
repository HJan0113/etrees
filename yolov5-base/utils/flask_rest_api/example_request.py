# YOLOv5 🚀 by Ultralytics, GPL-3.0 license
"""
Perform test request
"""

import pprint

import requests

# DETECTION_URL = "http://localhost:5000/v1/object-detection/yolov5s"
DETECTION_URL = "/exp11_0515/weights/g.pt" # lzs alter
IMAGE = "test.jpg"


# Read image
with open(IMAGE, "rb") as f:
    image_data = f.read()

response = requests.post(DETECTION_URL, files={"image": image_data}).json()

pprint.pprint(response)
