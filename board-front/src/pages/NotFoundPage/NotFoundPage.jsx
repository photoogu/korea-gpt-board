/**@jsxImportSource @emotion/react */
import * as s from './style';
import { BiError } from 'react-icons/bi';

function NotFoundPage(props) {
    return (
        <div css={s.layout}>
            <BiError />
            <h1>Not Found Page</h1>
            <p>This is not the web page you are looking for.</p>
        </div>
    );
}

export default NotFoundPage;