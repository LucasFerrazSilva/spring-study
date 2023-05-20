CREATE TABLE TB_APPOINTMENTS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    PATIENT_ID BIGINT NOT NULL,
    DOCTOR_ID BIGINT NOT NULL,
    APPOINTMENT_TIME DATETIME NOT NULL,
    FOREIGN KEY (PATIENT_ID) REFERENCES TB_PATIENTS(ID),
    FOREIGN KEY (DOCTOR_ID) REFERENCES TB_DOCTORS(ID)
);