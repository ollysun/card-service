import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import LoginDetails from "./LoginDetails";
import Personalia from "./Personalia";
import Iframe from "./Iframe";


function RegistrationLayout() {
    return (
        <Row className="mx-0">
            <Col><LoginDetails /></Col>
            <Col><Personalia /></Col>
            <Col><Iframe /></Col>
        </Row>
    );
}

export default RegistrationLayout;