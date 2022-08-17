import Form from "react-bootstrap/Form";

function LastName() {
    return (
        <Form.Group className="mb-4" controlId="lastName">
            <Form.Label className="Form-label-required">First name <span className="Required"> *</span></Form.Label>
            <Form.Control type="text" placeholder="Last name"/>
        </Form.Group>
    );
}

export default LastName;