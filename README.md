<!-- PROJECT LOGO -->
<br />
<div align="center">
    <img src="./images/logo.png" alt="Logo" width="80" height="80">


<h1 align="center">PixStream</h1>
  <p align="center">
    A tag based media streaming site. Functionality includes tag based search, video streaming, audio streaming, and image browsing.
    <br />
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#goals">Goals</a></li>
        <li><a href="#layout">Layout</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#installation">Set Up</a>
      <ul>
        <li><a href="#frontend">FrontEnd</a></li>
        <li><a href="#tailwind-compiler">Tailwind Compiler</a></li>
        <li><a href="#database-setup">Database Setup</a></li>
      </ul>
    </li>
    <!--<li><a href="#documentation">Documentation</a></li>-->
    <li><a href="#dependencies">Dependencies</a></li>
    <li><a href="#contributors">Contributors</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project is an image board style web application that utilizes tag-based searches, allowing users to view content for videos, audio and images. Tags are labels associated with specific files of a particular classification. For example, searching and appending the tag "door<!-- example tag -->" to the active search would filter out all website content and display only those files associated with the "door<!-- example tag-->" tag.



### Goals

- Display images, video, and audio seamlessly together in a visually appealing way, while taking advantage of tag labels to make searching intuative and simple.



### Layout

#### Homepage
![homepage-screenshot]

#### Media Page (audio)

![media-page-screenshot]

#### Media Page (video)

![media-page]

#### Tags Page

![tags-page]

#### Upload Page

![upload-page]



### Built With

[![React][React.js]][React-url]

[![Tailwind][Tailwind.com]][Tailwind-url]



<!-- SET UP -->
## How to Setup
See the [Setup Guide](https://github.com/OntarioTech-CS-program/w23-csci2020u-project-team16/wiki/GUIDE:-How-to-Setup)


<!-- DOCUMENTATION -->
## Documentation

Check the [wiki][Wiki]


<!-- DEPENDENCIES -->
## Dependencies

- [Jackson][Jackson]:

    - jackson-annotations [maven][Jackson-Annotations]

    - jackson-databind [maven][Jackson-Databind]

- [ImageMagick][Image-Magick]:

    - We're using [im4java][im4java]

    - Recommended [binary for Windows][Magick-Binary-For-Windows] <!-- Zip file not working-->

    - make sure it has Convert, Magick, Identify... etc

- [FFmpeg][FFmpeg]

    - ffmpeg-cli-wrapper [maven][FFmpeg-Wrapper]

    - recommended [binary for windows][FFmpeg-Binary-For-Windows] <!-- Not found -->

    - make sure is has FFmpeg, and FFprobe

- [Postgres][Postgres]

    - postgresql [maven][Postgresql]

    - postgres [docker][Postgres-Docker]

- [Tinylog2](https://tinylog.org/v2/)

    - [download](https://tinylog.org/v2/download/)




<!-- MARKDOWN LINKS & IMAGES -->
[homepage-screenshot]: ./images/homepage-preview.png
[media-page-screenshot]: ./images/audio-media-view-preview.png
[media-page]: ./images/video-preview.gif
[tags-page]: ./images/tags-page-preview.gif
[upload-page]: ./images/upload-preview.gif
[React.js]: https://www.vectorlogo.zone/logos/reactjs/reactjs-ar21.svg
[React-url]: https://reactjs.org/
[Tailwind.com]: https://www.vectorlogo.zone/logos/tailwindcss/tailwindcss-ar21.svg
[Tailwind-url]: https://tailwindcss.com/
[Node.js]: https://nodejs.org
[Docker.com]: https://www.docker.com/
[Wiki]: https://github.com/OntarioTech-CS-program/w23-csci2020u-project-team16/wiki
[Jackson]: https://github.com/FasterXML
[Jackson-Annotations]: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
[Jackson-Databind]: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
[Image-Magick]: https://imagemagick.org/index.php
[im4java]: https://mvnrepository.com/artifact/org.im4java/im4java
[Magick-Binary-For-Windows]: https://imagemagick.org/archive/binaries/ImageMagick-7.1.1-3-portable-Q8-x64.zip
[FFmpeg]: https://ffmpeg.org/
[FFmpeg-Wrapper]: https://github.com/bramp/ffmpeg-cli-wrapper
[FFmpeg-Binary-For-Windows]: https://github.com/BtbN/FFmpeg-Builds/releases/download/autobuild-2023-03-16-20-18/ffmpeg-n4.4.3-3-gb48951bd29-win64-gpl-4.4.zip
[Postgres]: https://www.postgresql.org/
[Postgresql]: https://mvnrepository.com/artifact/org.postgresql/postgresql
[Postgres-Docker]: https://hub.docker.com/_/postgres
