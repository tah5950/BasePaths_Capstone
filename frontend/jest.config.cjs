module.exports = {
  testEnvironment: 'jest-fixed-jsdom',
  setupFilesAfterEnv: ["<rootDir>/src/setupTests.js"],
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy"
  },
  transform: {
    "^.+\\.[tj]sx?$": "babel-jest",  
    "^.+\\.(png|jpg|jpeg|gif|svg)$": "jest-transform-stub"
  },
};