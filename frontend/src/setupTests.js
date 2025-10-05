import '@testing-library/jest-dom';

global.importMetaEnv = {
    VITE_API_BASE_URL: 'http://localhost:8080'
};

Object.defineProperty(global, 'import', {
    value: {
        meta: {
            env: global.importMetaEnv
        }
    }
});

jest.mock("./config");