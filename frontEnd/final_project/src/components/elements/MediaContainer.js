import React from 'react'

import { DISPLAY_TYPES,
    API_TEMPLATES,
    NO_MEDIA_IMG,
    MAX_DIMENSIONS,
    MIME_IMG_CUTOFF } from '../../constants';
import { formatStringB } from '../../requests';
import VideoPlayer from './Video';


export default function VideoContainer(props)
{
    // define props that we'll use for display
    const { 
        link,
        hash, 
        hlsDomain, 
        displayType, 
        metaData:{
            id,
            title,
            description,
            created_at,
            mime_int,
            duration,
            width,
            height
        }
    } = props;
    
    // tailwind is not letting us use custom height/width for images, not sure why
    // so this function will give you the width needed to get the desired height
    function getWidthForDesiredHeight(oldWidth,oldHeight,newHeight)
    {
        const ratio = newHeight/oldHeight;

        return (oldWidth * ratio);
    }

    const thumb = React.useRef("");

    function imgError(image) 
    {
        // set the source to no media image, disregard error
        thumb.current.setAttribute("src", NO_MEDIA_IMG);
        return true;
    }

    function thumbnailPreview()
    {
        // should return div that is of a set width and height
        // div should contain image, which has at least one of its dimensions equal to widht or height of container
        
        //get thumbnail url
        let thumb_url;
        if(link)
        {
            thumb_url = link;
        }
        else 
        {
            thumb_url = formatStringB(API_TEMPLATES.get_thumbnail.url, hash);
        }


        // this needs to be swapped out for code that does an aspect ratio check
        // const dx = Math.abs(max_width - props.metaData.width);
        // const dy = Math.abs(max_height - props.metaData.height);
        const dx = props.metaData.width;
        const dy = props.metaData.height;

        console.log("rendering thing");
        // checking if width needs to be max, or height
        if (dx > dy)
        {
            // display width should = max_width
            // make it a fixed width and height for consistent tiling
            return (
                <div className="inline-block w-[256px] h-[256px] flex justify-center items-center">
                    <img
                        // className="border-dashed border-2 border-white"
                        ref={thumb}
                        src={thumb_url}
                        width={MAX_DIMENSIONS.preview_width}
                        onError={imgError}
                        loading="lazy"
                        style={{ objectFit: "contain" }}
                    />
                  {/* <div className="text-custom-white caption">caption</div> */}
                </div>
              );
        }
        else
        {
            // display height should = max_height
            const newWidth = getWidthForDesiredHeight(props.metaData.width, props.metaData.height, MAX_DIMENSIONS.preview_height);

            return (
                <div className="inline-block w-[256px] h-[256px] flex justify-center items-center">
                    <img
                        // className="border-dashed border-2 border-white"
                        ref={thumb}
                        src={thumb_url}
                        width={newWidth}
                        onError={imgError}
                        loading="lazy"
                    />
                {/* <div className="text-custom-white caption">caption</div> */}
                </div>
            );
        }


        
    }
    
    function generalDisplay()
    {
        // set the url to get media data from
        let media_url;
        if(link)
        {
            media_url = link;
        }
        else 
        {
            media_url = formatStringB(API_TEMPLATES.get_file.url, hash);
        }

        const dx = props.metaData.width;
        const dy = props.metaData.height;

        if (dx > dy)
        {
            // display width should = max_width
            // make it a fixed width and height for consistent tiling

            // it is a video
            if (props.metaData.mime_int > MIME_IMG_CUTOFF)
            {
                // const hlsUrl = link ? link : "";
                const caption = "this is the caption";
                return (
                    <div className={"inline-block w-["+ MAX_DIMENSIONS.general_width + 
                    "px] h-[" + MAX_DIMENSIONS.general_height + "px] flex justify-center items-center"} loading="lazy">
                        <VideoPlayer hlsUrl={media_url} hlsDomain={hlsDomain}/>
                        {/* <div className="text-custom-white caption">{caption}</div> */}
                    </div>
                )
            }
            return (
                <div className={"inline-block w-["+ MAX_DIMENSIONS.general_width + 
                    "px] h-[" + MAX_DIMENSIONS.general_height + "px] flex justify-center items-center"}>
                    <img
                        // className="border-dashed border-2 border-white"
                        ref={thumb}
                        src={media_url}
                        width={MAX_DIMENSIONS.general_width}
                        onError={imgError}
                        loading="lazy"
                        style={{ objectFit: "contain" }}
                    />
                  {/* <div className="text-custom-white caption">caption</div> */}
                </div>
              );
        }
        else
        {
            // display height should = max_height
            const newWidth = getWidthForDesiredHeight(props.metaData.width, props.metaData.height, MAX_DIMENSIONS.general_height);
            // it is a video
            if (props.metaData.mime_int > MIME_IMG_CUTOFF)
            {
                // const hlsUrl = link ? link : "";
                const caption = "this is the caption";
                return (
                    <div className={"inline-block w-["+ newWidth + 
                    "px] h-[" + MAX_DIMENSIONS.general_height + "px] flex justify-center items-center"} loading="lazy">
                        <VideoPlayer hlsUrl={media_url} hlsDomain={hlsDomain}/>
                        <div className="text-custom-white caption">{caption}</div>
                    </div>
                )
            }
            return (
                <div className={"inline-block w-["+ MAX_DIMENSIONS.general_width + 
                    "px] h-[" + MAX_DIMENSIONS.general_height + "px] flex justify-center items-center"}>
                    <img
                        // className="border-dashed border-2 border-white"
                        ref={thumb}
                        src={media_url}
                        width={newWidth}
                        onError={imgError}
                        loading="lazy"
                        style={{ objectFit: "contain" }}
                    />
                  {/* <div className="text-custom-white caption">caption</div> */}
                </div>
              );
        }
    }

    function fullDisplay()
    {
        // no functionality yet, plan is for this to display with no restriction on width or height, displaying media at true resolution
        return (<div></div>)
    }

    // call correct function based on the display type given in props
    function getCorrectDisplay(displayType)
    {
        console.log("displayType:"+displayType)
        switch (displayType)
        {
            case DISPLAY_TYPES.thumb_preview:
                return thumbnailPreview();
            case DISPLAY_TYPES.general_display:
                return generalDisplay();
            case DISPLAY_TYPES.full_size_display:
                return fullDisplay();
            default:
                // if they don't give anything, or somethign invalid, just assume they want thumbnail preview
                console.log("invalid display type: " + displayType);
                return thumbnailPreview();
        }
    }

    // call getCorrectDisplay so that we return just the desired media element
    return getCorrectDisplay(displayType);
}