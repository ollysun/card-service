import Form from "react-bootstrap/Form";

function EmailField() {
    return (
        <Form.Group className="mb-4 mt-1" controlId="formBasicEmail">
            <Form.Label className="Form-label-required">Email <span className="Required"> *</span></Form.Label>
            <Form.Control type="email" placeholder="Email"/>
        </Form.Group>
    );
}

export default EmailField;