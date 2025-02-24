import { useState } from "react"

export const useInputValid = ({regexp, errorText}) => { // 함수 이름이 use 로 시작 >> hook 함수
    const [ name, setName ] = useState("");
    const [ value, setValue ] = useState("");
    const [ errorMessage, setErrorMessage ] = useState("");

    const handleOnChange = (e) => {
        setName(e.target.name);
        setValue(e.target.value);
    }

    const handleOnBlur = () => {
        // if(!regexp.test(value)) {
        //     setErrorMessage(errorText);
        // }
        const text = regexp.test(value) ? "" : errorText;
        setErrorMessage(text);
    }

    return { name, value, errorMessage, handleOnBlur, handleOnChange }; // >> js 에서는 "name": name, "value": value 와 같이 key 값이 변수명으로 자동 지정됨
}