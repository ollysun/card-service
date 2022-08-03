const getIframeContentBody = () => {
    return cy.get('iframe[data-cy="card-iframe"]')
        .its('0.contentDocument.body').should('not.be.empty')
        .then(cy.wrap)
}

describe('register profile', () => {

    it('should contains iframe', () => {
        cy.visit('/')
        cy.get('.App-iframe')
        cy.get('iframe')
    })

    it('iframe should contain content', function () {
        cy.visit('/')
        console.log(getIframeContentBody())
        getIframeContentBody()
    });

    it('iframe should contain some label', function () {
        cy.visit('/')
        getIframeContentBody().contains('h6', 'Add Card')
        getIframeContentBody().contains('label', 'Card Number')
        getIframeContentBody().contains('label', 'Expiry Date')
        getIframeContentBody().contains('button', 'Save Card')
        getIframeContentBody().find('#saveCard').should('have.text', 'Save Card')
        getIframeContentBody().find('#cardNumber').focus().type("32437848848448")
        getIframeContentBody().find('#cardBanner')
            .should('have.css', 'background-color','rgb(173, 194, 238)')
    });

})