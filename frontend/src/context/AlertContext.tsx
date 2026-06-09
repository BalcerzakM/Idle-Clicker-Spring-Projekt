import {createContext, useContext, useEffect, useRef, useState} from "react";
import type {ReactNode} from "react";
import "../css/GameAlertView.css";

interface AlertContextType {
    showError: (message: string) => void;
    showInfo: (message: string) => void;
    showLevelUp: (message: string) => void;
}

const AlertContext = createContext<AlertContextType | undefined>(undefined);

export function AlertProvider({children}: {children: ReactNode}) {
    type AlertType = "error" | "info" | "levelUp";
    const [alert, setAlert] = useState<{
        message: string;
        type: AlertType;
    } | null>(null);
    const buttonRef = useRef<HTMLButtonElement>(null);

    const showError = (message: string) => {
        setAlert({
            message,
            type: "error",
        });
    };

    const showInfo = (message: string) => {
        setAlert({
            message,
            type: "info",
        });
    };

    const showLevelUp = (message: string) => {
        setAlert({
            message,
            type: "levelUp",
        });
    };

    useEffect(() => {
        if (alert) {
            buttonRef.current?.focus();
        }
    }, [alert]);

    return (
        <AlertContext.Provider value={{ showError, showInfo, showLevelUp }}>
            {children}

            {alert && (
                <div className="game-alert-overlay">
                    <div className={`game-alert ${alert.type}`}>
                        <h2>
                            {{
                                error: "BŁĄD",
                                info: "KOMUNIKAT",
                                levelUp: "AWANS!"
                            }[alert.type]}
                        </h2>

                        <p>{alert.message}</p>

                        <button
                            ref={buttonRef}
                            onClick={() => setAlert(null)}
                        >
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