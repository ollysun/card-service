import Container from "react-bootstrap/Container";
import {FC} from "react";

interface IframeProps {
    title: string,
    src: string
}

const Iframe: FC<IframeProps> = (props) : JSX.Element => {
    return (
        <Container className="px-3">
            <iframe className="App-iframe" title={props.title}
                    src={props.src}/>
        </Container>
    );
}
export default Iframe;