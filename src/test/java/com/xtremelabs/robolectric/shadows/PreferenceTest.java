package com.xtremelabs.robolectric.shadows;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import com.xtremelabs.robolectric.internal.TestAttributeSet;

@RunWith(WithTestDefaultsRunner.class)
public class PreferenceTest {

	private TestPreference preference;
	private ShadowPreference shadow;

	private Context context;
	private TestAttributeSet attrs;
	
	@Before
	public void setup() {
		context = new Activity();
		attrs = new TestAttributeSet( new HashMap<String, String>() );
		preference = new TestPreference(context, attrs);
		shadow = Robolectric.shadowOf( preference );
	}

	@Test
	public void testConstructors() {
		int defStyle = 7;
		
		preference = new TestPreference(context, attrs, defStyle);
		shadow = Robolectric.shadowOf(preference);		
		assertThat(shadow.getContext(), sameInstance(context));
		assertThat(shadow.getAttrs(), sameInstance((AttributeSet)attrs));
		assertThat(shadow.getDefStyle(), equalTo(defStyle));	
		
		preference = new TestPreference(context, attrs);
		shadow = Robolectric.shadowOf(preference);		
		assertThat(shadow.getContext(), sameInstance( context ));
		assertThat(shadow.getAttrs(), sameInstance((AttributeSet)attrs));
		assertThat(shadow.getDefStyle(), equalTo(0));		
	}
	
	@Test
	public void testShouldPersist() {
		boolean[] values = { true, false };
		
		for( boolean shouldPersist : values ) { 
			shadow.setShouldPersist(shouldPersist);
			assertThat(preference.shouldPersist(), equalTo(shouldPersist));
		}
	}
	
	@Test
	public void testPersistedInt() {
		int defaultValue = 727;
		int[] values = { 0, 1, 2, 2011 };
		
		for(int persistedInt : values) {			
			shadow.setPersistedInt(persistedInt);
			
			shadow.setShouldPersist(false);
			assertThat(preference.getPersistedInt(defaultValue), equalTo(defaultValue));
			
			shadow.setShouldPersist(true);
			assertThat(preference.getPersistedInt(defaultValue), equalTo(persistedInt));			
		}
	}
	
	private static class TestPreference extends Preference {
	
		public TestPreference(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}
		   
		public TestPreference(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		public boolean shouldPersist() {
			return super.shouldPersist();
		}
		
		public int getPersistedInt(int defaultReturnValue) {
			return super.getPersistedInt(defaultReturnValue);
		}	
	}
}
