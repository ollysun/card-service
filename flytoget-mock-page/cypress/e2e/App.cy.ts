const cardNumber = '65423456789767892';
const expiryMonth = '02';
const expiryYear = '24';

const getIframeContentBody = () => {
    return cy.get('iframe[data-cy="card-iframe"]')
        .its('0.contentDocument.body').should('not.be.empty')
        .then(cy.wrap)
}

describe('register profile', () => {

    beforeEach(() => {
        cy.visit('/')
    })

    it('should contains iframe', () => {
        cy.get('.App-iframe')
        cy.get('iframe')
    })

    it.skip('iframe should contain content', function () {
        getIframeContentBody()
    });

    it.skip('iframe should contain some labels', function () {
        getIframeContentBody().contains('h6', 'Add Card')
        getIframeContentBody().contains('label', 'Card Number')
        getIframeContentBody().contains('label', 'Expiry Date')
    });

    it.skip('iframe should contain save card button', function () {
        getIframeContentBody().contains('button', 'Save Card')
    });

    it.skip('iframe should load a save card button', function () {
        getIframeContentBody().find('#saveCard').should('have.text', 'Save Card')
    });

    it.skip('account number field should be disabled by default', function () {
        getIframeContentBody().find('#accountNumberContainer').should('have.css', 'display', "none")
    });

    it.skip('populate the fields', () => {
        getIframeContentBody().find('#cardNumber').type(cardNumber).should('have.text', cardNumber)
        getIframeContentBody().find('#expiryMonth').type(expiryMonth).should('have.text', expiryMonth)
        getIframeContentBody().find('#expiryYear').type(expiryYear).should('have.text', expiryYear)
    })

})