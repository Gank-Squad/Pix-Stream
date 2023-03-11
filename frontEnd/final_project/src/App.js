
import React from 'react';
import {BrowserRouter as Router,Routes,Route} from "react-router-dom";

import './App.css';

import Default from './components/pages/Default';

import { ROUTES } from './constants';

function App() {
    const [state, setState] = React.useState({ 
        cookies : undefined // we can get cookies from a library and put them here
      });
      
      return (
        <Router>
          <Routes>
          <Route path={ROUTES.default} element={<Default {...state} />}></Route>
          </Routes>
        </Router>
      );
}

export default App;
