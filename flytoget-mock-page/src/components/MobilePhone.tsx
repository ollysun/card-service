import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";

function MobilePhone() {
    return (
        <Form.Group className="mb-3" controlId="mobilePhone">
            <Form.Label className="Form-label-required">Mobile Phone <span className="Required"> *</span></Form.Label>
            <InputGroup>
                <Form.Select className ="Mobile-code" aria-label="+47">
                    <option value="+47">+47 </option>
                    <option value="+31">+31 </option>
                    <option value="+9">+9 </option>
                    <option value="+1">+1 </option>
                    <option value="+34">+34 </option>
                    <option value="+32">+32 </option>
                    <option value="+12">+12</option>
                </Form.Select>

                <Form.Control aria-label="Mobile Phone" placeholder="Mobile Phone"/>
            </InputGroup>
            <Form.Text id="Country-name" muted className="mt-0"> Norge </Form.Text>
        </Form.Group>
    );
}

export default MobilePhone;