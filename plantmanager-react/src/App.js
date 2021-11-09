import logo from './logoPlantManager.png';
import './App.css';
import Login from './components/Login/Login';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import Homepage from './components/Homepage/Homepage';

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";


function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route exact path="/" element={<Homepage />} />
                    <Route path="/login/:where" element={<Login />} />
                    <Route path="/forgotPassword" element={<ForgotPassword />} />
                </Routes>
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <p>
                        Edit <code>src/App.js</code> and save to reload.
                    </p>
                    <a
                        className="App-link"
                        href="https://reactjs.org"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        Learn React
                    </a>
                </header>
            </div>
        </Router>
    );
}

export default App;
