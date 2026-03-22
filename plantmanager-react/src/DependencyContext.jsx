import {createContext, useContext} from 'react';

const DependencyContext = createContext({});

export const useDependencies = () => {
    return useContext(DependencyContext);
};

export const DependencyProvider = ({children, ...services}) => {
    return (
        <DependencyContext.Provider value={services}>
            {children}
        </DependencyContext.Provider>
    );
}