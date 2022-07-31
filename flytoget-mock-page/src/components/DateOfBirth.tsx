import Form from "react-bootstrap/Form";

function DateOfBirth() {
    return (
        <Form.Group className="mb-4" controlId="dateOfBirth">
            <Form.Label className="Form-label-required mt-2">Date of Birth</Form.Label>
            <Form.Control type="date" placeholder="dd.mm.yy"/>
        </Form.Group>
    );
}

export default DateOfBirth;