/**@jsxImportSource @emotion/react */
import * as s from './style';

function ValidInput({
    type,
    name,
    placeholder,
    value,
    onChange,
    onFocus = null,
    regexp,
    errorMessage,
    inputValidError,
    setInputValidError
}) {
    
    const handleOnBlur = () => {
        setInputValidError(prev => ({
            ...prev,
            [name]: !regexp.test(value), // 외부에서 받아오는 name 대신 e 를 받아와서 e.target.name 으로 해도 됨
        }));
    }
    
    return (
        <div css={s.groupBox}>
            <input css={s.textInput}
                type={type}
                name={name}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                onFocus={onFocus}
                onBlur={handleOnBlur}
            />
            {
                !!inputValidError[name] &&
                <p css={s.messageText}>{errorMessage}</p>
            }
        </div>
    );
}

export default ValidInput;