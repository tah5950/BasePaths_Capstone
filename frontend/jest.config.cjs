module.exports = {
  testEnvironment: 'jest-fixed-jsdom',
  setupFilesAfterEnv: ["<rootDir>/src/setupTests.js"],
  transform: {
    "^.+\\.[tj]sx?$": "babel-jest",  
    "^.+\\.(png|jpg|jpeg|gif|svg)$": "jest-transform-stub"
  },
};