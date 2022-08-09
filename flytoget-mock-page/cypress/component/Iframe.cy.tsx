import Iframe from "../../src/components/Iframe";

describe('Iframe.cy.ts', () => {

    const title = process.env.REACT_APP_IFRAME_TITLE || "card registration iframe"
    const src = process.env.REACT_APP_IFRAME_URL || " http://localhost:8000"

  it('playground', () => {
    cy.mount(<Iframe title={title} src={src} />);
  });

    it("should load card registration content", () => {
        cy.mount(<Iframe title={title} src={src} />)
        cy.get('iframe[data-cy="card-iframe"]')
            .its('0.contentDocument.body').should('not.be.empty')
    })

    it.skip("should contains Add card test", () => {
        cy.mount(<Iframe title={title} src={src} />)
        cy.get('iframe[data-cy="card-iframe"]')
            .its('0.contentDocument.body').contains("Add Card")
    })
})