package com.syiw.insurance.Model;

import java.util.Optional;

public record ClaimInput(
        Integer id,
        String name,
        String surname,
        String phoneNumber,
        Optional<String> address,
        Integer policyNumber,
        Integer claimType,
        String description
) {
}
