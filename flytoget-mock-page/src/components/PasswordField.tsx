import Form from "react-bootstrap/Form";

function PasswordField() {
    return (
        <Form.Group className="mb-3" controlId="password">
            <Form.Label className="Form-label-required">
                Password (min. 8 characters) <span className="Required"> *</span>
            </Form.Label>
            <Form.Control type="password" placeholder="Password"/>
            <div className="Password-strength mt-1"></div>
        </Form.Group>
    );
}

export default PasswordField;