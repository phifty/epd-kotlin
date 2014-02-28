
# Encrypted Profile Documents

The encrypted profile document (EPD) format acts as an encryption container of information that the author wants to
keep private or share with a defined set of other EPD authors. It enables the author to clearly separate private and
shared data and keep control about what information can be accessed by others. This is achieved by a combination of
symmetric and asymmetric encryption methods, which are chained together in the process of encrypting and decrypting
(locking and unlocking) an EPD. Each EPD contains, beside the payload data and some encryption overhead, also
information about the author and his signature, which enables a random reader to verify the authenticity of the EPD,
even if the content cannot be unlocked.

This library can handle EPDs and is implemented in pure Javascript. It's compatible with the latest versions of Firefox
and Chrome.

## Contribution

Any contribution is very welcome. If code is contributed, please make sure that tests are included.
