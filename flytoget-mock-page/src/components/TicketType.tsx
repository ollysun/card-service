import Form from "react-bootstrap/Form";

function TicketType() {
    return (
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label className="Form-label-required">Ticket type <span className="Required"> *</span></Form.Label>
            <Form.Select className="form-control" placeholder="Ticket Type" aria-label="Ticket type">
                <option>Ticket type</option>
                <option value="1">1 way</option>
                <option value="2">2 way</option>
            </Form.Select>
        </Form.Group>
    );
}

export default TicketType;