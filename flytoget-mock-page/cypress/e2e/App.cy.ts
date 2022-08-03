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

    it('iframe should contain content', function () {
        console.log(getIframeContentBody())
        getIframeContentBody()
    });

    it('iframe should contain some labels', function () {
        getIframeContentBody().contains('h6', 'Add Card')
        getIframeContentBody().contains('label', 'Card Number')
        getIframeContentBody().contains('label', 'Expiry Date')
    });

    it('iframe should contain save card button', function () {
        getIframeContentBody().contains('button', 'Save Card')
    });

    it('iframe should load a save card button', function () {
        getIframeContentBody().find('#saveCard').should('have.text', 'Save Card')
    });

    it('account number field should be disabled by default', function () {
        getIframeContentBody().find('#accountNumberContainer').should('have.css', 'display', "none")
    });

})