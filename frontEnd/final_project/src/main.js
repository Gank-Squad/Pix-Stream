//*****************************************************NAVIGATION BAR *****************************************************
// ------------------------------------------ JS for toggle buttons & navbar and main content sliding  -------------------------------
const navbar = document.getElementById('navbar-background');
const toggleButton = document.getElementById('navbar-toggle');
const content = document.getElementById('content');
const toggleX = document.getElementById('toggle-x');
const toggleBurger = document.getElementById('toggle-burger');
const titleSpacer = document.getElementById('titleSpacer');
const slideLength = 350;
const slideSpeed = 0.2;


// Slides navbar out when cursor is within 20px of the left edge of the screen
// Slides navbar back in when cursor is farther than slideLengthpx from the left edge of the screen
//this will only be enabled if the toggle button has not been clicked

function shiftOut(duration, slideDistance){
    navbar.style.left = '0px';
    content.style.marginLeft = `${slideDistance}px`;
    navbar.style.transitionDuration = `${duration}s`;
    content.style.transitionDuration = `${duration}s`;

    let toggleXLeft = 17 + slideDistance - 50;
    let toggleButtonLeft = 10 + slideDistance - 50;
    toggleButton.style.left = `${toggleButtonLeft}px`;
    toggleX.style.left = `${toggleXLeft}px`;
    toggleX.style.opacity = 1;
    
    // titleSpacer.style.width = `${600 - slideDistance}px`;

    toggleBurger.style.display = 'none';    
}
function shiftIn(duration, slideDistance){
    navbar.style.left = `-${slideDistance}px`;
    content.style.marginLeft = '0';
    navbar.style.transitionDuration = `${duration}s`;
    content.style.transitionDuration = `${duration}s`;
    
    toggleButton.style.left = `10px`;
    toggleX.style.left = `17px`;
    toggleX.style.opacity = 0;

    setTimeout(() => {
        toggleBurger.style.display = '';
    }, (duration * 1000)-100);

    // titleSpacer.style.width = `${600 + slideDistance}px`;
}

//manually toggles the navbar when the toggle button is clicked
toggleButton.addEventListener('click', () => {
    toggleButton.classList.toggle('active');
    if(toggleButton.classList.contains('active')){
        shiftOut(slideSpeed, slideLength);
    } else {
        shiftIn(slideSpeed, slideLength);
    }
});

// automatically slide the navbar in and out based on mouse position
//not too sure if this is necessary at the moment, but it works
// window.addEventListener('mousemove', (event) => {
//     if(!toggleButton.classList.contains('active')){ 
//         if (event.clientX < 20) {
//             shiftOut(slideSpeed, slideLength);
//           } else if (event.clientX > slideLength) {
//             shiftIn(slideSpeed, slideLength);
//         }
//     }
// });

//closes the navbar if its is extended and the toggle button has been clicked
//this also works but im not sure if this is necessary at the moment
content.addEventListener('click', () => {
    if(toggleButton.classList.contains('active')){
        toggleButton.classList.toggle('active');
        shiftIn(slideSpeed, slideLength);
    }
});


// ------------------------------------------ Moving Tags between the context box and the Selected box -------------------------------
const selectedTagBox = document.getElementById('selected-tagbox');
const contextTagBox = document.getElementById('context-tagbox');
const tags = document.querySelectorAll('.tag');

function appendTag(tag){
    
    const row = tag.closest('tr');
    const button = tag.getElementsByTagName('button').item(0);

    if(contextTagBox.contains(tag)){
        contextTagBox.querySelector('tbody').removeChild(row);
        selectedTagBox.querySelector('tbody').appendChild(row);
        button.classList.replace("fa-plus","fa-minus");
    } else {
        selectedTagBox.querySelector('tbody').removeChild(row);
        contextTagBox.querySelector('tbody').appendChild(row);
        tag.getElementsByTagName('button').item(0).classList.replace("fa-minus","fa-plus");
    }
};

window.addEventListener('load', () => {
    tags.forEach(element => {
        element.getElementsByTagName('button').item(0).addEventListener('click', () => {
        appendTag(element);
        });
    })
});

// ------------------------------------------ Tag Filtering ------------------------------------------
const tagSearch = document.getElementById('tag-search');
function filterTags(entry){
    let contextTags = contextTagBox.querySelectorAll('.tag');
    contextTags.forEach(element => {
        if(element.getElementsByTagName('label').item(0).textContent.includes(entry)){
            element.closest('tr').style.display = 'inherit';
        }
        else{
            element.closest('tr').style.display = 'none';
        }
    });
}
tagSearch.addEventListener('keyup', () => {
    filterTags(tagSearch.value);
});

// ------------------------------------------ Tag Clear Button ------------------------------------------
const tagClearButton = document.getElementById('clearbtn');

tagClearButton.addEventListener('click', () => {
    selectedTagBox.querySelectorAll('.tag').forEach(element => {
        appendTag(element);
    });
});


//***************************************************** MAIN PAGE CONTENT *****************************************************
// ------------------------------------------ Tag Clear Button ------------------------------------------
// const typedAnimation = new Typed('#typed-animation', {
//     strings: ['\nWatch Videos.', '\nStream Music.','\nBrowse Images.', ''],
//     typeSpeed: 100,
//     loop: true,
//     loopCount: 1,
//     backSpeed: 40,
//     showCursor: true,
//     cursorChar: '|',
//     smartBackspace: true,
// });
// window.addEventListener('load', typedAnimation);




