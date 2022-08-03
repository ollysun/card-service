import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import LoginDetails from "./LoginDetails";
import Personalia from "./Personalia";
import Iframe from "./Iframe";

function RegistrationLayout() {
    const title = process.env.REACT_APP_IFRAME_TITLE || "card registration iframe"
    const src = process.env.REACT_APP_IFRAME_URL || " http://localhost:8000"
    return (
    <Row className="mx-0">
            <Col><LoginDetails /></Col>
            <Col><Personalia /></Col>
            <Col><Iframe title={title}  src={src}/></Col>
        </Row>
    );
}

export default RegistrationLayout;