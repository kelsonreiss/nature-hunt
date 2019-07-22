# Code sample found at https://aiforearth.portal.azure-api.net/docs/services/5b183aa7d114573c1fd1d383/operations/classify

import http.client, urllib.request, urllib.parse, urllib.error, base64
import io
from PIL import Image

headers = {
    # Request headers
    # Change this if going to use JSON
    'Content-Type': 'application/octet-stream',
    'Ocp-Apim-Subscription-Key': '949e7dc4318041af961810828dadcf05',
}

params = urllib.parse.urlencode({
    # Parameters
    # Number of results to return
    'topK' : '1',
    # Prediction Mode
    # Can be either classifyOnly or classifyAndDetect
    'predictMode' : 'classifyOnly'
})

# Try converting to bytes?
#https://stackoverflow.com/questions/14759637/python-pil-bytes-to-image

# REPLACE HERE WITH IMAGE THAT YOU WANT TO USE
imageFilename = '/Users/kelson/Downloads/buttfly.JPG'

imageFileObj = open(imageFilename, "rb")
imageBinaryBytes = imageFileObj.read()

# Copy and Pasted from API site
try:
    conn = http.client.HTTPSConnection('aiforearth.azure-api.net')
    conn.request("POST", "/species-recognition/v0.1/predict?%s" % params, imageBinaryBytes, headers)
    response = conn.getresponse()
    data = response.read()
    print(data)
    conn.close()
except Exception as e:
    print("[Errno {0}] {1}".format(e.errno, e.strerror))