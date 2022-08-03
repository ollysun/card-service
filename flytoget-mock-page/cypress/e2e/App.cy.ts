describe('register profile', () => {
  beforeEach( () => {
    cy.visit('/')
  })

  it('should contains iframe', () => {
    cy.get('.App-iframe')
    cy.get('iframe')
  })

 const getIframeContentBody = () => {
    return cy.get('.App-iframe')
        .its('0.contentDocument').should('exist')
        .its('body').should('not.be.undefined')
        .then(cy.wrap)
  }

  it('iframe should contain content', function () {
    getIframeContentBody()
  });

  it('iframe should contain some label', function () {
    getIframeContentBody().contains('h6', 'Add Card')
    getIframeContentBody().contains('label', 'Card Number')
    getIframeContentBody().contains('label', 'Expiry Date')
    getIframeContentBody().contains('button', 'Save Card')
  });

})