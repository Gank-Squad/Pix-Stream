
import React from "react";
import { API_ENDPOINTS } from "../../constants";
export default function TagSidebar(props) 
{
    const { searchCallback, hideSearchButton, searchButtonPressed } = props;


    // basically global variables / instance variables 
    const [search, setSearch] = React.useState([]);
    const [tags, setTags] = React.useState(null);

    const TAGS_API = API_ENDPOINTS.media.get_tags;

    // html elements that we will be using 
    const tagSearch = React.useRef("");
    const selectedTagBox = React.useRef("");

    const tagClearButton = React.useRef();
    const tagSearchButton = React.useRef();

    const contextTagBox = React.useRef("");

    React.useEffect(() => {

        fetch(TAGS_API, { 'method': 'get' })
            .then(response => response.json())
            .then(json => {

                setTags(json);
            })
            .catch(e => console.log(e));

    }, []);


    if (tags == null) {
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

    function tagButtonPressed(e, tag) {
        if (!e || !e.target)
            return;

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


    return (


        <div className="px-4 ">

            <div className="tagbox-container">
                <header className="tagbox-header">
                    <label className="text-custom-white font-bold">Selected Tags</label><br/>
{/* bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l */}
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
                    </div>
                    

                    
                </header>

                <table ref={selectedTagBox} className="tagbox">
                    <tbody>

                    </tbody>
                </table>
            </div>

            <div id="search-box">
                <input onChange={searchValueKeyDown} ref={tagSearch} id="tag-search" type="text" placeholder="Tag Search..."></input>
            </div>

            <div className="tagbox-container">
                <header className="tagbox-header">
                    <label className="text-custom-white font-bold">Context Tags</label>
                </header>

                <table ref={contextTagBox} className="tagbox">
                    <tbody>


                        {
                        tags.map((tag, index) => {
                            return <tr key={index} index={index} tag-id={tag.tag_id}>
                                <td>
                                    <div className="tag">
                                        <i className="fa fa-tag"></i>
                                        <button onClick={(e) => tagButtonPressed(e, tag)} 
                                        className="text-custom-white text-ellipsis hover:bg-button-depressed truncate rounded px-2 w-48 text-left">
                                            <label>
                                                {tag.namespace + ":" + tag.subtag}
                                            </label>
                                        </button>                                        
                                    </div>
                                </td>
                            </tr>
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

