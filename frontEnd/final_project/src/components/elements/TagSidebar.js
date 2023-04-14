
import React from "react";
import { API_ENDPOINTS, API_TEMPLATES } from "../../constants";
import { formatStringB } from "../../requests";
export default function TagSidebar(props) 
{
    const { createTagButton, searchCallback, hideSearchButton, 
            searchButtonPressed, selectedTagIds,
            displayOnlyMode, displayTags
        } = props;


    // basically global variables / instance variables 
    const [tags, setTags] = React.useState([]);
    const [selectedTags, setSelectedTags] = React.useState([]);

    const TAGS_API = API_ENDPOINTS.media.get_tags;

    // html elements that we will be using 
    const tagSearch = React.useRef("");
    const selectedTagBox = React.useRef("");

    const tagClearButton = React.useRef();
    const tagSearchButton = React.useRef();

    const contextTagBox = React.useRef("");

    // log changes to tags
    React.useEffect(() => {
        console.log("All tags: ", tags);
    }, [tags]);

    // log changes to selected tags, update search
    React.useEffect(() => {
        console.log("Updating search with Selected tags: ", selectedTags);
        updateSearch();
    }, [selectedTags]);


    // on page load, fetch tags from api
    React.useEffect(() => {

        if(selectedTagIds)
            setSelectedTags(selectedTagIds);

        if(displayOnlyMode)
            return;

        fetch(TAGS_API, { 'method': 'get' })
            .then(response => response.json())
            .then(json => {

                setTags(json);

            })
            .catch(e => console.log(e));

    }, []);

    // if theres no tags to display, let user know
    if ((tags == null || tags.length === 0) && !displayOnlyMode) {
        return <p>Loading...</p>;
    }

    // standard functions 
    function clearSelected() 
    {
        setSelectedTags([]);
    }

    // display tags
    function filterTags(entry) {
        let contextTags = contextTagBox.current.querySelectorAll('tr');

        contextTags.forEach(element => {
            const label = element.getElementsByTagName('label').item(0);

            if (!label)
                return;

            if (label.textContent.includes(entry)) {
                element.style.display = "inherit";
            }
            else {
                element.style.display = "none";
            }
        });
    }

    // when button is pressed, add tag to selected tag
    function addOnlySelectedTag()
    {
        const entry = tagSearch.current.value;
        let contextTags = contextTagBox.current.querySelectorAll('tr');

        let onlyElement = null;
        //iterate through all the context tags
        for(let element of contextTags)
        {
            const label = element.getElementsByTagName('label').item(0);

            if (!label)
                continue;

            if (label.textContent.includes(entry)) 
            {
                if(onlyElement != null)
                    return -1;

                onlyElement = element;
            }
        }

        if(onlyElement == null)
            return -1;

        
        const id = onlyElement.getAttribute("tag-id");

        if(!id) 
            return -1;

        return id;
    }

    function updateSearch() 
    {
        let s = selectedTags;

        // if(selectedTagIds)
        // {
        //     s = s.concat(selectedTagIds);
        // }
        
        const search = s.map(tag_id => {

            let i = tags.findIndex(x => x.tag_id === tag_id);

            return tags[i];
        });

        if(searchCallback)
        {
            searchCallback(search);
        }
    }

    function onKeyEnter(e)
    {
        if(e.keyCode !== 13)
            return;

        const id = addOnlySelectedTag();

        if(id === -1)
        {
            createTagFromSearchBar();
        }
        else if(parseInt(id))
        {
            updateTagSelection({"tag_id" : parseInt(id)});
            tagSearch.current.value = "";
            filterTags("");
        }
    }

    function searchValueKeyDown(e) 
    {
     
        if(displayOnlyMode)
            return;
        filterTags(tagSearch.current.value);
    }

    // function searchButtonPressed(e) {
    //     filterTags(tagSearch.current.value);
    // }

    function clearButtonPressed(e) {
        clearSelected();
        updateSearch();
    }


    async function createTagFromSearchBar(e)
    {
        if(displayOnlyMode)
            return;
        const tag = tagSearch.current.value.trim();
        const spl = tag.split(":", 2);
    
        let namespace = "";
        let subtag = "";

        if(spl.length === 1)
        {
            subtag = spl[0];
        }
        else 
        {
            namespace = spl[0];
            subtag = spl[1];
        }

        if(subtag === "")
            return;

        const json = [
            {
                "namespace" : namespace,
                "subtag" : subtag
            }
        ]
        
        await fetch(API_TEMPLATES.create_tag.url, {
            "method" : "POST",
            "headers" : {
                "content-type" : "application/json"
            },
            "body" : JSON.stringify(json)
        })
        .then(r => {
            if(r.status !== 200)
            return Promise.reject("server");

            return r.json()
        })
        .then(json => 
        {
            // behold!!! the power of javascript! !!!! 
            let  added = new Set();
            for(let tag of json)
            {
                if(tags.findIndex(x => x.tag_id === tag.tag_id) === -1)
                {
                    added.add(tag);
                }
            }            

            setTags(old => [...old, ...added]);

            for(let tag of json)
            {
                updateTagSelection(tag, true);
            }
            updateSearch();

            tagSearch.current.value = "";
            filterTags("");

        }).catch(e => {
            if(e === "server") return;
            console.log(e)
        });
    }

    function redirectToSearchPage(tags)
    {
        if(!tags || tags.length <= 0)
            return;

        const url = formatStringB('/results?tags={IDS}', tags.map(tag => tag.tag_id).join(","))
    
        window.location.href = url;
    }

    function updateTagSelection(tag)
    {
        if(displayOnlyMode)
        {
            redirectToSearchPage([tag]);
            return;
        }
            

        if(selectedTags.includes(tag.tag_id))
        {
            setSelectedTags(old => old.filter(t => t != tag.tag_id));
        }
        else 
        {
            setSelectedTags(old => [...old, tag.tag_id]);
        }
    }


    function getClickableTagHTML(tag, index)
    {
        let display =tag.subtag;
        if(tag.namespace !== "")
        {
            display = tag.namespace + ":" + tag.subtag; 
        }

        return <tr key={index} index={index} tag-id={tag.tag_id}>
        <td>
            <div className="tag">
                <i className="fa fa-tag"></i>
                <button title={display} onClick={(e) => updateTagSelection(tag)} 
                className="text-custom-white text-ellipsis hover:bg-button-depressed truncate rounded px-2 w-48 text-left">
                    <label>
                        {
                            display
                        }
                    </label>
                </button>                                        
            </div>
        </td>
    </tr>
    }


    return (
        <div className="px-4 h-full">

{!displayOnlyMode &&            <div className="tagbox-container">
                <header className="tagbox-header">
                    <div className="inline-flex">

                        {
                            !hideSearchButton && 
                            <button 
                                ref={tagSearchButton} 
                                onClick={searchButtonPressed} 
                                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-0 px-1 rounded-l">
                            Search
                        </button>
                        }
                        
                        <button onClick={clearButtonPressed} ref={tagClearButton} className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-0 px-1 rounded-r">
                            Clear</button>

                        {createTagButton &&
                            <button 
                                onClick={createTagFromSearchBar} 
                                className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-0 px-1 rounded-l">
                            Create Tag
                        </button>
                        }    
                    </div>

                     <div id="search-box">
                <input 
                onKeyUp={onKeyEnter}
                onChange={searchValueKeyDown} ref={tagSearch} id="tag-search" type="text" placeholder="Tag Search...">

                </input>
            </div>
                </header>


                   <label className="text-custom-white font-bold">Selected Tags</label>
                    <br/>
            <table ref={selectedTagBox} className="tagbox">
                <tbody>
                    {
                        
                    tags.map((tag, index) => {

                        if(!selectedTags)
                            return;

                        if(selectedTags && selectedTags.includes(tag.tag_id))
                        {
                            return getClickableTagHTML(tag, index);
                        }
                        return;
                    })}



                    </tbody>
                </table>

            </div>}

          

            <div className="tagbox-container">
                <header className="tagbox-header">
                    <label className="text-custom-white font-bold">Context Tags</label>
                </header>

                <table ref={contextTagBox} className="tagbox">
                    <tbody>
                        {!displayOnlyMode&&
                        tags.map((tag, index) => {
                            if(selectedTags && selectedTags.includes(tag.tag_id))
                            {
                                return;
                            }
                                // return;

                            return getClickableTagHTML(tag, index);

                        })}
                        
                    {displayOnlyMode && displayTags && displayTags.map((tag, index) => 
                    {
                        return getClickableTagHTML(tag, index);
                    })}
                    </tbody>
                </table>
            </div>


            {/* <div className="navbar-spacer"></div>

            <div className="link"><a href="about.html">
                <i className="fa fa-info-circle"></i>About</a>
            </div> */}

        </div>
    );
}

