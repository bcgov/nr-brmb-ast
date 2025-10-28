create or replace function farms_webapp_pkg.decrypt(
    in in_string text
) returns text
language plpgsql
as
$$
declare

    v_encrypted bytea;
    v_decrypted bytea;
    v_key text;
    v_clean text;

begin
    if in_string is not null and in_string <> '' then
        -- Decode base64 input to binary
        v_encrypted := decode(in_string, 'base64');

        -- Get your encryption key
        v_key := farms_webapp_pkg.get_encryption_key();

        -- Decrypt (must match algorithm from encrypt)
        v_decrypted := farms_webapp_pkg.decrypt(v_encrypted, v_key::bytea, '3des');

        -- Convert binary to escaped text, remove nulls and other padding
        v_clean := replace(encode(v_decrypted, 'escape'), E'\\000', '');

        -- Return readable text (escaped binary decoded back to normal string)
        return convert_from(v_clean::bytea, 'UTF8');
    end if;

    return v_return_string;
end;
$$;
