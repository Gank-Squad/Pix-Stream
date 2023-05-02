import React from 'react'

import VideoPlayer from './Video';

// Deprecated code, do not use
// use MediaContainer instead
export default function VideoContainer(props)
{
    const { hlsUrl, hlsDomain, caption, style  } = props;

    function imgError(image) 
    {
        return true;
    }

    // return the video
    return (
        <div className="file-container" style={style} loading="lazy">
                <VideoPlayer hlsUrl={hlsUrl} hlsDomain={hlsDomain}/>
                <div className="text-custom-white caption">{caption}</div>
        </div>
    )
}