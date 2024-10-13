import './App.css';
import Login from './components/Login/Login';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import Homepage from './components/Homepage/Homepage';
import AppHeader from './components/AppHeader/AppHeader';
import {DependencyProvider} from "./DependencyContext";
import AuthService from "./services/AuthService/AuthService";
import PlantService from "./services/PlantService/PlantService";
import RegistrationService from "./services/RegistrationService/RegistrationService";
import ValidationService from "./services/ValidationService/ValidationService";

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Dashboard from "./components/Dashboard/Dashboard";
import PlantEditor from "./components/PlantEditor/PlantEditor";

import axios from 'axios';


function App() {
    return (
        <Router>
            <DependencyProvider authService={AuthService}
                                registrationService={RegistrationService}
                                validationService={ValidationService}
                                plantService={PlantService}>
                <div className="App" data-testid="App">
                    <AppHeader/>
                    <Routes>
                        <Route path="/" element={<Homepage/>}/>
                        <Route path="/login/:where" element={<Login/>}/>
                        <Route path="/forgotPassword" element={<ForgotPassword/>}/>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                        <Route path="/dashboard/add" element={<PlantEditor/>}/>
                    </Routes>
                </div>
            </DependencyProvider>
        </Router>
    );
}

axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = token; 
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default App;
