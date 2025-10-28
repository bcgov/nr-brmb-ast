create or replace function farms_webapp_pkg.encrypt(
    in in_string text
) returns text
language plpgsql
as
$$
declare

    v_string text := in_string;
    v_padded text := in_string;
    v_key text;
    v_encrypted bytea;
    v_return_string text := null;

begin
    if in_string is not null and in_string <> '' then
        -- Pad to a multiple of 8 bytes (DES block size)
        while length(v_padded) % 8 <> 0 loop
            v_padded := v_padded || chr(0);
        end loop;

        -- Get encryption key (your key retrieval function)
        v_key := farms_webapp_pkg.get_encryption_key();

        -- Encrypt using DES algorithm (for compatibility with Oracle)
        v_encrypted := farms_webapp_pkg.encrypt(v_padded::bytea, v_key::bytea, '3des');

        -- Encode to base64 for storage or transmission
        v_return_string := encode(v_encrypted, 'base64');
    end if;

    return v_return_string;
end;
$$;
