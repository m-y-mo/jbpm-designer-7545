/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.designer.client.popup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockito;
import org.jbpm.designer.client.resources.i18n.DesignerEditorConstants;
import org.jbpm.designer.client.shared.AssignmentRow;
import org.jbpm.designer.client.util.ListBoxValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityDataIOEditorViewImplTest {

    @Mock
    private ActivityDataIOEditorWidget inputAssignmentsWidget;

    @Mock
    private ActivityDataIOEditorWidget outputAssignmentsWidget;

    @Captor
    private ArgumentCaptor<List<AssignmentRow>> listAssignmentCaptor;

    @Captor
    ArgumentCaptor<Map<String, List<String>>> mapCaptor;

    @GwtMock
    private ActivityDataIOEditorViewImpl view;

    private List<AssignmentRow> rows;

    @Captor
    private ArgumentCaptor<ListBoxValues> valuesCaptor;

    private ActivityDataIOEditorView.Presenter presenter = new ActivityDataIOEditor();

    @Before
    public void setUp() {
        GwtMockito.initMocks(this);
        view.inputAssignmentsWidget = inputAssignmentsWidget;
        view.outputAssignmentsWidget = outputAssignmentsWidget;
        view.presenter = presenter;

        doCallRealMethod().when(view).setInputAssignmentRows(any(List.class));
        doCallRealMethod().when(view).setOutputAssignmentRows(any(List.class));
        doCallRealMethod().when(view).setPossibleInputAssignmentsDataTypes(anyList());
        doCallRealMethod().when(view).setPossibleOutputAssignmentsDataTypes(anyList());
        doCallRealMethod().when(view).setInputAssignmentsProcessVariables(anyList());
        doCallRealMethod().when(view).setCustomAssignmentsProperties(anyMap());
        doCallRealMethod().when(view).setOutputAssignmentsProcessVariables(anyList());
        doCallRealMethod().when(view).setInputAssignmentsDisallowedNames(anySet());
        doCallRealMethod().when(view).setIsInputAssignmentSingleVar(anyBoolean());
        doCallRealMethod().when(view).setIsOutputAssignmentSingleVar(anyBoolean());
        doCallRealMethod().when(view).setInputAssignmentsVisibility(anyBoolean());
        doCallRealMethod().when(view).setOutputAssignmentsVisibility(anyBoolean());
        doCallRealMethod().when(view).getInputAssignmentData();
        doCallRealMethod().when(view).getOutputAssignmentData();

        rows = new ArrayList<AssignmentRow>();
        rows.add(new AssignmentRow("varName",
                                   null,
                                   null,
                                   null,
                                   "varName",
                                   null));
        rows.add(new AssignmentRow("varName2",
                                   null,
                                   null,
                                   null,
                                   "varName2",
                                   null));
    }

    @Test
    public void testInputAssignmentsRowsSameSourceAndTargetName() {
        view.setInputAssignmentRows(rows);
        verify(inputAssignmentsWidget,
               times(1)).setData(listAssignmentCaptor.capture());
        verify(outputAssignmentsWidget,
               never()).setData(any(List.class));
        verifyForSameSourceAndTargetName();
    }

    @Test
    public void testOutputAssignmentsRowsSameSourceAndTargetName() {
        view.setOutputAssignmentRows(rows);
        verify(outputAssignmentsWidget,
               times(1)).setData(listAssignmentCaptor.capture());
        verify(inputAssignmentsWidget,
               never()).setData(any(List.class));
        verifyForSameSourceAndTargetName();
    }

    @Test
    public void testSetPossibleInputAssignmentsDataTypes() {
        List<String> dataTypes = Arrays.asList("String");
        view.setPossibleInputAssignmentsDataTypes(dataTypes);
        verify(inputAssignmentsWidget).setDataTypes(valuesCaptor.capture());
        List<String> typesWithCustomValue = valuesCaptor.getValue().getAcceptableValuesWithCustomValues();
        assertEquals(3,
                     typesWithCustomValue.size());
        assertTrue(typesWithCustomValue.containsAll(Arrays.asList("",
                                                                  "Custom ...",
                                                                  "String")));
    }

    @Test
    public void testSetPossibleOutputAssignmentsDataTypes() {
        List<String> dataTypes = Arrays.asList("String");
        view.setPossibleOutputAssignmentsDataTypes(dataTypes);
        verify(outputAssignmentsWidget).setDataTypes(valuesCaptor.capture());
        List<String> typesWithCustomValue = valuesCaptor.getValue().getAcceptableValuesWithCustomValues();
        assertEquals(3,
                     typesWithCustomValue.size());
        assertTrue(typesWithCustomValue.containsAll(Arrays.asList("",
                                                                  "Custom ...",
                                                                  "String")));
    }

    @Test
    public void testSetPossibleInputAssignmentsVariables() {
        List<String> variables = Arrays.asList("variable");
        view.setInputAssignmentsProcessVariables(variables);
        verify(inputAssignmentsWidget).setProcessVariables(valuesCaptor.capture());
        List<String> variablesWithCustomValue = valuesCaptor.getValue().getAcceptableValuesWithCustomValues();
        assertEquals(3,
                     variablesWithCustomValue.size());
        assertTrue(variablesWithCustomValue.containsAll(Arrays.asList("",
                                                                      "Constant ...",
                                                                      "variable")));
    }

    @Test
    public void testSetPossibleOutputAssignmentsVariables() {
        List<String> variables = Arrays.asList("variable");
        view.setOutputAssignmentsProcessVariables(variables);
        verify(outputAssignmentsWidget).setProcessVariables(valuesCaptor.capture());
        List<String> variablesWithCustomValue = valuesCaptor.getValue().getAcceptableValuesWithCustomValues();
        assertEquals(3,
                     variablesWithCustomValue.size());
        assertTrue(variablesWithCustomValue.containsAll(Arrays.asList("",
                                                                      "Constant ...",
                                                                      "variable")));
    }

    @Test
    public void testSetCustomAssignmentsProperties() {
        Map<String, List<String>> customAssignmentsProperties = new HashMap<String, List<String>>();
        customAssignmentsProperties.put("From",
                                        Arrays.asList(new String[]{"Mary", "Maria", "Melissa"}));
        customAssignmentsProperties.put("To",
                                        Arrays.asList(new String[]{"Tom"}));
        customAssignmentsProperties.put("CC",
                                        Arrays.asList(new String[]{"James", "John"}));

        view.setCustomAssignmentsProperties(customAssignmentsProperties);
        verify(inputAssignmentsWidget).setCustomAssignmentsProperties(mapCaptor.capture());
        assertEquals(3,
                     mapCaptor.getValue().keySet().size());
        assertEquals(customAssignmentsProperties.get("From"),
                     mapCaptor.getValue().get("From"));
        assertEquals(customAssignmentsProperties.get("To"),
                     mapCaptor.getValue().get("To"));
        assertEquals(customAssignmentsProperties.get("CC"),
                     mapCaptor.getValue().get("CC"));

    }

    @Test
    public void testSetInputAssignmentsDisallowedNames() {
        Set<String> names = new HashSet<>(Arrays.asList("nameA",
                                                        "nameB"));
        view.setInputAssignmentsDisallowedNames(names);
        verify(inputAssignmentsWidget).setDisallowedNames(names,
                                                          DesignerEditorConstants.INSTANCE.This_input_should_be_entered_as_a_property_for_the_task());
    }

    @Test
    public void testSetInputAssignmentsVisibility() {
        view.setInputAssignmentsVisibility(true);
        verify(inputAssignmentsWidget).setIsVisible(true);
        verify(inputAssignmentsWidget,
               never()).setIsVisible(false);

        view.setInputAssignmentsVisibility(false);
        verify(inputAssignmentsWidget).setIsVisible(true);
        verify(inputAssignmentsWidget).setIsVisible(false);
    }

    @Test
    public void testSetOutputAssignmentsVisibility() {
        view.setOutputAssignmentsVisibility(true);
        verify(outputAssignmentsWidget).setIsVisible(true);
        verify(outputAssignmentsWidget,
               never()).setIsVisible(false);

        view.setOutputAssignmentsVisibility(false);
        verify(outputAssignmentsWidget).setIsVisible(true);
        verify(outputAssignmentsWidget).setIsVisible(false);
    }

    @Test
    public void testSetInputAssignmentsSingleVar() {
        view.setIsInputAssignmentSingleVar(true);
        verify(inputAssignmentsWidget).setIsSingleVar(true);
        verify(inputAssignmentsWidget,
               never()).setIsSingleVar(false);

        view.setIsInputAssignmentSingleVar(false);
        verify(inputAssignmentsWidget).setIsSingleVar(true);
        verify(inputAssignmentsWidget).setIsSingleVar(false);
    }

    @Test
    public void testSetOutputAssignmentsSingleVar() {
        view.setIsOutputAssignmentSingleVar(true);
        verify(outputAssignmentsWidget).setIsSingleVar(true);
        verify(outputAssignmentsWidget,
               never()).setIsSingleVar(false);

        view.setIsOutputAssignmentSingleVar(false);
        verify(outputAssignmentsWidget).setIsSingleVar(true);
        verify(outputAssignmentsWidget).setIsSingleVar(false);
    }

    @Test
    public void testGetInputAssignemntsData() {
        when(inputAssignmentsWidget.getData()).thenReturn(rows);
        assertEquals(rows,
                     view.getInputAssignmentData());
    }

    @Test
    public void testGetOutputAssignemntsData() {
        when(outputAssignmentsWidget.getData()).thenReturn(rows);
        assertEquals(rows,
                     view.getOutputAssignmentData());
    }

    private void verifyForSameSourceAndTargetName() {
        assertEquals(2,
                     listAssignmentCaptor.getValue().size());
        assertEquals(rows.get(0),
                     listAssignmentCaptor.getValue().get(0));
        assertEquals("varName",
                     listAssignmentCaptor.getValue().get(0).getName());
        assertEquals("varName",
                     listAssignmentCaptor.getValue().get(0).getProcessVar());
        assertEquals(rows.get(1),
                     listAssignmentCaptor.getValue().get(1));
        assertEquals("varName2",
                     listAssignmentCaptor.getValue().get(1).getName());
        assertEquals("varName2",
                     listAssignmentCaptor.getValue().get(1).getProcessVar());
    }
}