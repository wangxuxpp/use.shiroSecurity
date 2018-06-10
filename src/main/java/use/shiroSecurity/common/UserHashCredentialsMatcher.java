package use.shiroSecurity.common;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.StringUtils;

/**
 * 自定义加密认证
 * 项目名称:wmes
 * 类型名称:DoozerHashCredentialsMatcher
 * 类型描述:
 * 作者:wx
 * 创建时间:2018年5月23日
 * @version:
 * Copyright(C)XuWang.All Rights Reserved.
 */
public class UserHashCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * @since 1.1
     */
    private String hashAlgorithm;
    private int hashIterations;
    private boolean hashSalted;
    private boolean storedCredentialsHexEncoded;

 
    public UserHashCredentialsMatcher() {
        this.hashAlgorithm = null;
        this.hashSalted = false;
        this.hashIterations = 1;
        this.storedCredentialsHexEncoded = true; //false means Base64-encoded
    }

    
    public UserHashCredentialsMatcher(String hashAlgorithmName) {
        this();
        if (!StringUtils.hasText(hashAlgorithmName) ) {
            throw new IllegalArgumentException("hashAlgorithmName cannot be null or empty.");
        }
        this.hashAlgorithm = hashAlgorithmName;
    }

   
    public String getHashAlgorithmName() {
        return hashAlgorithm;
    }

   
    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithm = hashAlgorithmName;
    }

    
    public boolean isStoredCredentialsHexEncoded() {
        return storedCredentialsHexEncoded;
    }

    
    public void setStoredCredentialsHexEncoded(boolean storedCredentialsHexEncoded) {
        this.storedCredentialsHexEncoded = storedCredentialsHexEncoded;
    }

    
    public boolean isHashSalted() {
        return hashSalted;
    }

    public void setHashSalted(boolean hashSalted) {
        this.hashSalted = hashSalted;
    }

    
    public int getHashIterations() {
        return hashIterations;
    }

   
    public void setHashIterations(int hashIterations) {
        if (hashIterations < 1) {
            this.hashIterations = 1;
        } else {
            this.hashIterations = hashIterations;
        }
    }

   
    protected Object getSalt(AuthenticationToken token) {
        return token.getPrincipal();
    }

    
    protected Object getCredentials(AuthenticationInfo info) {
        Object credentials = info.getCredentials();

        byte[] storedBytes = toBytes(credentials);

        if (credentials instanceof String || credentials instanceof char[]) {
            //account.credentials were a char[] or String, so
            //we need to do text decoding first:
            if (isStoredCredentialsHexEncoded()) {
                storedBytes = Hex.decode(storedBytes);
            } else {
                storedBytes = Base64.decode(storedBytes);
            }
        }
        SimpleHash hash = newHashInstance();
        hash.setBytes(storedBytes);
        return hash;
    }

   
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    	UsernamePasswordTokenAbstract usernamePasswordToken = (UsernamePasswordTokenAbstract)token;
    	Object tokenHashedCredentials = null;
    	if(usernamePasswordToken.getAuthorizationEd()){
    		tokenHashedCredentials = getCredentials(info);
    	}else{
    		tokenHashedCredentials = hashProvidedCredentials(token, info);
    	}
        Object accountCredentials = getCredentials(info);
        return equals(tokenHashedCredentials, accountCredentials);
    }

    
    protected Object hashProvidedCredentials(AuthenticationToken token, AuthenticationInfo info) {
        Object salt = null;
        if (info instanceof SaltedAuthenticationInfo) {
            salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
        } else {
            //retain 1.0 backwards compatibility:
            if (isHashSalted()) {
                salt = getSalt(token);
            }
        }
        return hashProvidedCredentials(token.getCredentials(), salt, getHashIterations());
    }

    
    private String assertHashAlgorithmName() throws IllegalStateException {
        String hashAlgorithmName = getHashAlgorithmName();
        if (hashAlgorithmName == null) {
            String msg = "Required 'hashAlgorithmName' property has not been set.  This is required to execute " +
                    "the hashing algorithm.";
            throw new IllegalStateException(msg);
        }
        return hashAlgorithmName;
    }

   
    protected Hash hashProvidedCredentials(Object credentials, Object salt, int hashIterations) {
        String hashAlgorithmName = assertHashAlgorithmName();
        return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
    }

    
    protected SimpleHash newHashInstance() {
        String hashAlgorithmName = assertHashAlgorithmName();
        return new SimpleHash(hashAlgorithmName);
    }

}
