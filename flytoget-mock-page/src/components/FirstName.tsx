import Form from "react-bootstrap/Form";

function FirstName() {
    return (
        <Form.Group className="mb-4" controlId="firstName">
            <Form.Label className="Form-label-required">First name <span className="Required"> *</span></Form.Label>
            <Form.Control type="text" placeholder="First name"/>
        </Form.Group>
    );
}

export default FirstName;