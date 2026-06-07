import {createContext, useContext, useEffect, useRef, useState} from "react";
import type {ReactNode} from "react";
import "../css/GameAlertView.css";

interface AlertContextType {
    showError: (message: string) => void;
}

const AlertContext = createContext<AlertContextType | undefined>(undefined);

export function AlertProvider({children}: {children: ReactNode}) {
    const [message, setMessage] = useState<string | null>(null);
    const buttonRef = useRef<HTMLButtonElement>(null);

    const showError = (message: string) => {
        setMessage(message);
    };

    useEffect(() => {
        if (message) {
            buttonRef.current?.focus();
        }
    }, [message]);

    return (
        <AlertContext.Provider value={{showError}}>
            {children}

            {message && (
                <div className={"game-alert-overlay"}>
                    <div className={"game-alert"}>
                        <p>{message}</p>

                        <button ref={buttonRef} onClick={() => setMessage(null)}>
                            OK
                        </button>
                    </div>
                </div>
            )}
        </AlertContext.Provider>
    )
}

export function useAlert() {
    const context = useContext(AlertContext);

    if (!context) {
        throw new Error("useAlert musi byc uzyty wewnatrz AlertProvider!");
    }

    return context;
}