# dogs-api-delta

**NOTE: The source code sits in the 'Master' branch and NOT the 'Main' branch**

You can use whatever architecture you like, but would like you to justify your choice in a Readme.md file. Be detailed in your readme about libraries used, choices made etc.

The app uses the open dogs api and is build in Kotlin for Android mobile devices with mvvm.
It is a single activity with fragments using the navigation component.
Data is fetched from the API with the help of Retroofit library.
Glide image library is used for fetching images from the return URLs from the API.
The app was to be native and consistant, so for image sharing, used an intent to have uniform functionality and it's pre-existing in android.
For the doawnload of the app, used the existing download manager to save the image to the user's pictures directory on android mobile device.
The app makes a service calla nd gets back a list of 20 random images through the use of a view model and the retrofit instance.
The list of images is passed on to the adapter. Where the rendering of each image is done using Glide.
Upon the click of a single image, the image url is passed into the next fragment as an argument.
The 2nd fragmement receives the argument and used Glide to render the image to the user.
The share button uses an intent to share the bitmap from the imageview
The download button onclick triggers the downlnoad manager to save the image inthe user's android gallery
Back navigation takes the user back to the list of images.
On the list of dogs screenn when the user gets to the bottom of the list, the scrolll listener triggers the service call again to get more dogs.






