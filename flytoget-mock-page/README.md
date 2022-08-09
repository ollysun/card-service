# Flytoget mock 
This is a [React](https://reactjs.org/) project module that simulate flytoget customer and card registration pages. It has an embedded iframe which
loads the card registration interface.
This module is meant to mock how the card registration interface behaves within a React Iframe based on Flytoget use case.

## Running the project

In the project directory, you can run the following npm command:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.

For more info, check out [running npm tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.


### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

For more information about production deployment, check out [React production deployment](https://facebook.github.io/create-react-app/docs/deployment).

## Cypress
Cypress is the testing framework used within the project.\
It is a next generation front end testing tool built for the modern web. 


### How to run tests
Navigate to the project module.

Run `npm cypress:open` for Cypress UI Test Runner.\
Or you can run `npm cypress:run` to run in CLI.

Alternatively you can run tests using npx.

Run `npx cypress open` for Cypress UI Test Runner.\
Or you can run `npx cypress run` to run in CLI.

For more information, check out [running cypress tests](https://docs.cypress.io/guides/guides/command-line)

### Create new test

Create a `.tsx` or `.ts` in cypress/e2e folder for writing end-to-end tests.\
For component test create a `.tsx` or `.ts` in cypress/component folder instead.

For more information about how to write cypress tests, check out [writing new tests](https://docs.cypress.io/guides/core-concepts/writing-and-organizing-tests)