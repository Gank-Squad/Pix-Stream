
import React from "react";
import { API_ENDPOINTS, API_TEMPLATES } from "../../constants";
export default function TagSidebar(props) 
{
    const { createTagButton, searchCallback, hideSearchButton, searchButtonPressed, selectedTagIds } = props;


    // basically global variables / instance variables 
    const [search, setSearch] = React.useState([]);
    const [tags, setTags] = React.useState([]);
    const [selectedTags, setSelectedTags] = React.useState([]);

    const TAGS_API = API_ENDPOINTS.media.get_tags;

    // html elements that we will be using 
    const tagSearch = React.useRef("");
    const selectedTagBox = React.useRef("");

    const tagClearButton = React.useRef();
    const tagSearchButton = React.useRef();

    const contextTagBox = React.useRef("");


    React.useEffect(() => {
        console.log(tags);
    }, [tags]);


    React.useEffect(() => {

        fetch(TAGS_API, { 'method': 'get' })
            .then(response => response.json())
            .then(json => {

                setTags(json);
                console.log(json);

            })
            .catch(e => console.log(e));

    }, []);


    if (tags == null || tags.length === 0) {
        return <p>Loading...</p>;
    }

    // standard functions 
    function clearSelected() {
        // selectedTagBox.current is the DOM element, selectedTagBox is the react object
        // selectedTagBox.current can be used like normal js
        const contextTags = selectedTagBox.current.querySelectorAll('tr');

        const tbody = contextTagBox.current.querySelector('tbody');

        contextTags.forEach(element => {
            tbody.appendChild(element);
        });
    }

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

    function updateSearch() 
    {
        const contextTags = selectedTagBox.current.querySelectorAll('tr');

        let s = [];
        contextTags.forEach(element => 
        {
            const i = element.getAttribute("index");

            if(i)
            {
                s.push(tags[i]);
            }
        });

        setSearch(prevItems => s);

        if(searchCallback)
        {
            searchCallback(s);
        }
    }

    function searchValueKeyDown(e) {
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
    const tag = tagSearch.current.value.trim();
    const spl = tag.split(":", 2);
   console.log(spl); 
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

    console.log(JSON.stringify(json));
    
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
        console.log(json);
       setTags(old => old.concat(json) );
       tagSearch.current.value = "";
       updateSearch();
       filterTags("");

       setSelectedTags(old => old.concat(json.map(x => x.tag_id)));
    }).catch(e => {
        if(e === "server") return;
        console.log(e)
    });
}

    function tagButtonPressed(e, tag) {

        const trSelected = selectedTagBox.current.querySelector(`tr[tag-id="${tag.tag_id}"]`);
        const trContext = contextTagBox.current.querySelector(`tr[tag-id="${tag.tag_id}"]`);

        if (trSelected) {
            contextTagBox.current.querySelector('tbody').appendChild(trSelected);
        }
        else {
            selectedTagBox.current.querySelector('tbody').appendChild(trContext);
        }

        updateSearch();
    }


    function getClickableTagHTML(tag, index)
    {
        return <tr key={index} index={index} tag-id={tag.tag_id}>
        <td>
            <div className="tag">
                <i className="fa fa-tag"></i>
                <button onClick={(e) => tagButtonPressed(e, tag)} 
                className="text-custom-white text-ellipsis hover:bg-button-depressed truncate rounded px-2 w-48 text-left">
                    <label>
                        {
                            function(){
                                if(tag.namespace === "")
                                {
                                    return tag.subtag;
                                }
                                return tag.namespace + tag.subtag;
                            }()
                        }
                    </label>
                </button>                                        
            </div>
        </td>
    </tr>
    }
     

    return (
        <div className="px-4 h-full">

            <div className="tagbox-container">
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
                <input onChange={searchValueKeyDown} ref={tagSearch} id="tag-search" type="text" placeholder="Tag Search..."></input>
            </div>
                </header>


                <label className="text-custom-white font-bold">Selected Tags</label><br/>

            <table ref={selectedTagBox} className="tagbox">
                <tbody>
                    {
                    tags.map((tag, index) => {

                        if(!selectedTagIds && !selectedTags)
                            return;

                        if(selectedTags && selectedTags.includes(tag.tag_id))
                        {
                            return getClickableTagHTML(tag, index);
                        }

                        if((selectedTagIds && selectedTagIds.includes(tag.tag_id)))
                        {
                            return getClickableTagHTML(tag, index);
                        }
                    })}
                    </tbody>
                </table>
            </div>

          

            <div className="tagbox-container">
                <header className="tagbox-header">
                    <label className="text-custom-white font-bold">Context Tags</label>
                </header>

                <table ref={contextTagBox} className="tagbox">
                    <tbody>
                        {
                        tags.map((tag, index) => {
                            if(selectedTags && selectedTags.includes(tag.tag_id) ||
                            selectedTagIds && selectedTagIds.includes(tag.tag_id))
                            {
                                return;
                            }
                                // return;

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

